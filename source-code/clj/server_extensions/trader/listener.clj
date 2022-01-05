
(ns server-extensions.trader.listener
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [prototypes.api    :as prototypes]
              [tea-time.core     :as tt]
              [x.server-core.api :as a]
              [server-extensions.trader.engine       :as engine]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (s)
(def TIMER-INTERVAL 5)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-source-code
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  []
  (get (mongo-db/get-document-by-id "trader" "listener-details")
       :trader/source-code))

(defn- run-listener!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [source-code (get-source-code)]
          (if-let [result (try (load-string source-code)
                               (catch       Exception e (str e)))]
                  (a/dispatch [:trader/log! :trader/listener result {:highlighted? true}]))
          (a/dispatch [:trader/log! :trader-listener "No source-code found error"
                                    {:warning? true}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- start-timer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  []
  (a/dispatch [:trader/log! :trader/listener "Listener initializing ..."])
  (tt/start!)
  (let [timer (tt/every! TIMER-INTERVAL (bound-fn [] (run-listener!)))]
       (a/dispatch [:db/set-item! [:trader :timer] timer]))
  (return :timer-started))

(defn- stop-timer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (object) timer
  ;
  ; @return (keyword)
  [timer]
  (a/dispatch [:trader/log! :trader/listener "Listener exiting ..."])
  (tt/cancel! timer)
  (tt/stop!)
  (a/dispatch [:db/remove-item! [:trader :timer]])
  (return :timer-stopped))

(defn- toggle-listener-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [env _]
  (if-let [timer (a/subscribed [:db/get-item [:trader :timer]])]
          (stop-timer! timer)
          (start-timer!)))

(defresolver toggle-listener!___
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/toggle-listener! (keyword)
             ;    :timer-started, :timer-stopped}
             [env resolver-props]
             {:trader/toggle-listener!___ (toggle-listener-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-listener-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:listener-active? (boolean)
  ;   :source-code (string)}
  [env _]
  {:listener-active? (a/subscribed [:db/item-exists? [:trader :timer]])
   :source-code      (get-source-code)})

(defresolver get-listener-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/get-listener-data (map)
             ;    {:listener-active? (boolean)
             ;     :source-code (string)}}
             [env resolver-props]
             {:trader/get-listener-data (get-listener-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-listener-data]); toggle-listener!])

(pathom/reg-handlers! :trader/listener HANDLERS)
