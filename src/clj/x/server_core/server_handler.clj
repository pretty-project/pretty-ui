;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v0.8.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.server-handler
    (:require [mid-fruits.candy             :refer [param]]
              [mid-fruits.map               :refer [dissoc-in]]
              [mid-fruits.string            :as string]
              [org.httpkit.server           :refer [run-server]]
              [x.app-details                :as details]
              [x.server-core.engine         :as engine]
              [x.server-core.event-handler  :as event-handler]
              [x.server-core.router-handler :refer [ring-handler]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (integer)
(def DEFAULT-PORT 3000)

; @constant (B)
(def MAX-BODY 1073741824)



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
  (merge {:port     DEFAULT-PORT
          ; {:max-body ...} is very important for projects which
          ; want to upload at least 1GB
          :max-body MAX-BODY}
         (param server-props)
         (if (string/nonempty? port)
             {:port (string/to-integer port)})))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- reset-server-state!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [::primary :meta-items :server-state]))

(event-handler/reg-event-db :x.server-core/reset-server-state! reset-server-state!)

(defn- store-server-state!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) server-state
  ;
  ; @return (map)
  [db [_ server-state]]
  (assoc-in db [::primary :meta-items :server-state] server-state))

(event-handler/reg-event-db :x.server-core/store-server-state! store-server-state!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- run-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;  {:join? (boolean)(opt)
  ;   :port (integer)(opt)}
  [server-props]
  (let [server-props (engine/prot server-props server-props-prototype)
        server-state (run-server (ring-handler)
                                 (param server-props))]
       (event-handler/dispatch [:x.server-core/store-server-state! server-state])

       ; *
       (let [server-port (get server-props :port)]
            (println details/app-name "started on port:" server-port))))

; @usage
;  [:x.server-core/run-server! {...}]
(event-handler/reg-handled-fx :x.server-core/run-server! run-server!)
