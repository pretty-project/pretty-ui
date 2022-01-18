
(ns server-extensions.storage.directory-handler
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-extensions.storage.engine      :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- create-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  [env {:keys [alias destination-id]}]
  (let [directory-item {:media/alias alias :media/content-size 0 :media/path [] :media/description ""
                        :media/items []    :media/mime-type "storage/directory"}]
       (if-let [{:media/keys [id]} (engine/insert-media-item! env directory-item)]
               (engine/attach-media-item! env destination-id id))))

(defmutation create-directory!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (?)
             [env mutation-props]
             {::pathom.co/op-name 'storage/create-directory!}
             (create-directory-f env mutation-props))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [create-directory!])

(pathom/reg-handlers! ::handlers HANDLERS)
