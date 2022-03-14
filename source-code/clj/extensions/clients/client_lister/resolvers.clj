
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.resolvers
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mid-fruits.candy                      :refer [param return]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [plugins.item-lister.api               :as item-lister]
              [x.server-locales.api                  :as locales]))



;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->name-field-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request]}]
  (let [name-order (locales/request->name-order request)]
       ; A :client/first-name és :client/last-name tulajdonságok sorrendjéhez a felhasználó által
       ; kiválaszott nyelv szerinti sorrendet alkalmazza.
       (case name-order :reversed {:client/name {:$concat [:$client/last-name  " " :$client/first-name]}}
                                  {:client/name {:$concat [:$client/first-name " " :$client/last-name]}})))

(defn env->get-pipeline
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [name-field-pattern (env->name-field-pattern env)
        ; - A :client/name virtuális mezőt szükséges hozzáadni a dokumentumokhoz a keresés és rendezés előtt!
        ; - A keresés és rendezés után szükséges eltávolítani a dokumentumokból a :client/name virtuális mezőt,
        ;   mert az item-lister plugin a törölt dokumentumok visszaállításakor a kliens-oldali változatot
        ;   küldi el a szerver részére, és ha az tartalmazná a virtuális mezőt, akkor a visszaállított
        ;   dokumentumok is tartalmaznák azt!
        env (-> env (pathom/env<-param :field-pattern name-field-pattern)
                    (pathom/env<-param :unset-pattern [:client/name]))]
       (item-lister/env->get-pipeline env :clients :client)))

(defn env->count-pipeline
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [name-field-pattern (env->name-field-pattern env)
        env (pathom/env<-param env :field-pattern name-field-pattern)]
       (item-lister/env->count-pipeline env :clients :client)))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env _]
  (let [get-pipeline   (env->get-pipeline   env)
        count-pipeline (env->count-pipeline env)]
       {:documents      (mongo-db/get-documents-by-pipeline   "clients" get-pipeline)
        :document-count (mongo-db/count-documents-by-pipeline "clients" count-pipeline)}))

(defresolver get-items
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env resolver-props]
             {:clients.client-lister/get-items (get-items-f env resolver-props)})



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-items])

(pathom/reg-handlers! ::handlers HANDLERS)
