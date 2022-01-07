
(ns server-extensions.trader.listener
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [prototypes.api    :as prototypes]
              [tea-time.core     :as tt]
              [x.server-core.api :as a]
              [server-extensions.trader.engine       :as engine]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-source-code
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  []
  (get (mongo-db/get-document-by-id "trader" "listener-details")
       :trader/source-code))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- source-code-environment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) source-code
  ;
  ; @return (string)
  [source-code]
  (let [market-data (a/subscribed [:db/get-item [:trader :market]])]
       ; - A (load-str "...") függvény a clojure.core névtérben hozza létre a konstansokat
       ; - A (load-str "...") függvény által lefuttatott forráskódban létrehozott konstansok és
       ;   átirányított függvények elérhetők lesznek a clojure.core névtérben.
       ; - A wrap-reload az átirányított függvényeket tartalmazó névtér újratöltésekor
       ;   hibát jelez, hogy az átirányított függvények nevei már foglaltak a clojure.core névtérben,
       ;   ezért szükséges azokat eltérő néven átirányítani.
       (str "(def market-data " market-data ")\n "
            "(def ^{:private true} mountain-highness server-extensions.trader.patterns/mountain-highness)\n "
            "(def ^{:private false} mountain-length   server-extensions.trader.patterns/mountain-length)\n "
            source-code)))

(defn run-source-code!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) source-code
  [source-code]
  (let [source-code (source-code-environment source-code)
        result (try (load-string source-code)
                    (catch       Exception e (a/dispatch [:trader/log! (str e) {:warning? true}])))]
       (if (string/nonempty? result)
           (a/dispatch [:trader/log! result {:highlighted? true}]))))

(defn run-listener!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [source-code (get-source-code)]
       (if (string/nonempty? source-code)
           (run-source-code! source-code)
           (a/dispatch [:trader/log! "No source-code found error" {:warning? true}]))))

(a/reg-fx :trader/run-listener! run-listener!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-listener-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:listener-active? (boolean)}
  [env _]
  (if (a/subscribed [:db/get-item [:trader :listener :listener-active?]])
      (do (a/dispatch-sync [:db/set-item! [:trader :listener :listener-active?] false])
          (a/dispatch      [:trader/log! "Deactivating listener ..."])
          {:listener-active? false
           :check (a/subscribed [:db/get-item [:trader :listener :listener-active?]])})
      (do (a/dispatch-sync [:db/set-item! [:trader :listener :listener-active?] true])
          (a/dispatch      [:trader/log! "Activating listener ..."])
          {:listener-active? true
           :check (a/subscribed [:db/get-item [:trader :listener :listener-active?]])})))

(defmutation toggle-listener!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (map)
             ;  {:listener-active? (boolean)}
             [env mutation-props]
             {::pco/op-name 'trader/toggle-listener!}
             (toggle-listener-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-listener-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:listener-active? (boolean)
  ;   :source-code (string)}
  [env _]
  {:listener-active? (boolean (a/subscribed [:db/get-item [:trader :listener :listener-active?]]))
   :source-code      (get-source-code)})

(defresolver download-listener-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/download-listener-data (map)
             ;    {:listener-active? (boolean)
             ;     :source-code (string)}}
             [env resolver-props]
             {:trader/download-listener-data (download-listener-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-listener-data toggle-listener!])

(pathom/reg-handlers! ::handlers HANDLERS)
