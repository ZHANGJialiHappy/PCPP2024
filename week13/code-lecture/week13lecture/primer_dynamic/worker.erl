% raup@itu.dk * 2024-11-19

-module(worker).
-export([start/1, init/1]).

% State
-record(worker_state, {primer_server}).

% Start
start(PrimerPID) ->
    spawn(?MODULE, init, [PrimerPID]).

% Initialization 
init(PrimerPID) ->
    State = #worker_state{primer_server=PrimerPID},
    loop(State).
    

% Behavior upon receiving messages
loop(State) ->
    receive
        {is_prime, Number} ->
            handle_is_prime(Number, State)
    end.

% 5. Message handlers
% 创建多个single job worker，让它们工作。single job worker工作完了直接发送结果给primer。（不要发还给worker，增加worker的负担）
handle_is_prime(Number, State = #worker_state{primer_server=PrimerPID}) ->
    SJO_PID = single_job_worker:start(),
    SJO_PID ! {check_prime, Number, PrimerPID},
    loop(State).
