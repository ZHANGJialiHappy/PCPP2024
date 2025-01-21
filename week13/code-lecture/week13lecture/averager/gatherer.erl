% raup@itu.dk * 2024-11-21

-module(gatherer).
-export([start/1,init/1]).

-record(gatherer_state, {top_gatherer, results}).

% State
start(GathererPID) ->
    spawn(?MODULE, init, [GathererPID]).

% Init
init(GathererPID) ->
    State = #gatherer_state{top_gatherer=GathererPID,
                            results=[]},
    loop(State).

% Loop
loop(State) ->
    receive
        {gather, Result} ->
            handle_gather(Result, State)
    end,
    loop(State).

%要注意多个scatter公用一个gatherer。[1]，[2]，[3]，[4]中只有[1]导到这个方法，[2]，[3]，[4]被导到下面的方法中。
handle_gather(Result, 
              State = #gatherer_state{results = Results}) 
  when Results == [] -> % State#gatherer_state.results 是一个空列表（即 Results == []）
    NewState = State#gatherer_state{results = [Result | Results]},
    loop(NewState);

handle_gather(Result, 
              #gatherer_state{results = Results,
                              top_gatherer = GathererPID}) ->
    R = lists:foldl(fun (X,Y) -> X+Y end, 0, [Result | Results]),
    GathererPID ! {gather, R}.
