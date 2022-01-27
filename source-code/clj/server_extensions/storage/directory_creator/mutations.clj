
(ns server-extensions.storage.directory-creator.mutations
    (:require [mongo-db.api :as mongo-db]
              [pathom.api   :as pathom]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [server-extensions.storage.engine      :as engine]))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  [env {:keys [alias destination-id]}]
  (if-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
          (let [destination-path (get  destination-item :media/path)
                directory-path   (conj destination-path {:media/id destination-id})
                directory-item {:media/alias alias :media/content-size 0 :media/description ""
                                :media/items []    :media/path directory-path
                                :media/mime-type "storage/directory"}]
               (when-let [{:media/keys [id]} (engine/insert-item! env directory-item)]
                         (engine/attach-item!             env destination-id id)
                         (engine/update-path-directories! env directory-item +)))))

(defmutation create-directory!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'storage.directory-creator/create-directory!}
             (create-directory-f env mutation-props))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [create-directory!])

(pathom/reg-handlers! ::handlers HANDLERS)
