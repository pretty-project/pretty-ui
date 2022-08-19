
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.server-handler.side-effects
    (:require [org.httpkit.server                      :refer [run-server]]
              [x.app-details                           :as details]
              [x.server-core.event-handler             :as event-handler]
              [x.server-core.router-handler.helpers    :refer [ring-handler]]
              [x.server-core.server-handler.prototypes :as server-handler.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn run-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;  {:dev-mode? (boolean)(opt)
  ;    Default: false
  ;   :join? (boolean)(opt)
  ;   :port (integer)(opt)}
  [server-props]
  (let [server-props (server-handler.prototypes/server-props-prototype server-props)
        server-state (-> (ring-handler)
                         (run-server server-props))]
       (event-handler/dispatch [:core/store-server-state! server-state])
       ; *
       (let [server-port (get server-props :port)]
            (println details/app-codename "started on port:" server-port))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/run-server! run-server!)
