
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.server-handler.side-effects
    (:require [mid-fruits.candy                    :refer [param]]
              [mid-fruits.string                   :as string]
              [org.httpkit.server                  :refer [run-server]]
              [x.app-details                       :as details]
              [x.server-core.event-handler         :as event-handler]
              [x.server-core.router-handler.engine :refer [ring-handler]]
              [x.server-core.server-handler.config :as server-handler.config]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- server-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;  {:port (integer or string)}
  ;
  ; @return (map)
  ;  {:port (integer)
  ;   :max-body (integer)
  [{:keys [port] :as server-props}]
  (merge {:port server-handler.config/DEFAULT-PORT
          ; {:max-body ...} is very important for projects which want to upload at least 1GB
          :max-body server-handler.config/MAX-BODY}
         (param server-props)
         (if (string/nonempty? port)
             {:port (string/to-integer port)})))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn run-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;  {:join? (boolean)(opt)
  ;   :port (integer)(opt)}
  [server-props]
  (let [server-props (server-props-prototype server-props)
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
