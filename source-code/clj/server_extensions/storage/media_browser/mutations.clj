
(ns server-extensions.storage.media-browser.mutations
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [server-extensions.storage.engine      :as engine]
              [server-plugins.item-browser.api       :as item-browser]))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (string)
  [env {:keys [item-id] :as mutation-props}]
  (if-let [file-item (engine/get-item env item-id)]
          (when-let [parent-id (item-browser/item->parent-id :storage :media file-item)]
                    (engine/detach-item!             env parent-id item-id)
                    (engine/remove-item!             env   item-id)
                    (engine/update-path-directories! env file-item -)
                    (return item-id))))

(defn delete-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (string)
  [env {:keys [item-id] :as mutation-props}]
  (if-let [directory-item (engine/get-item env item-id)]
          (when-let [parent-id (item-browser/item->parent-id :storage :media directory-item)]
                    (engine/detach-item!             env parent-id item-id)
                    (engine/remove-item!             env   item-id)
                    (engine/update-path-directories! env directory-item -)
                    (let [items (get directory-item :media/items)]
                         (doseq [{:media/keys [id]} items]
                                (when-let [{:media/keys [mime-type]} (engine/get-item env id)]
                                          (case mime-type "storage/directory" (delete-directory-f env {:item-id id})
                                                                              (delete-file-f      env {:item-id id}))
                                          (return item-id)))))))

(defn delete-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)}
  ;
  ; @return (?)
  [env {:keys [item-id] :as mutation-props}]
  (if-let [{:media/keys [mime-type]} (engine/get-item env item-id)]
          (case mime-type "storage/directory" (delete-directory-f env mutation-props)
                                              (delete-file-f      env mutation-props))))

(defn delete-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-ids (string in vector)}
  ;
  ; @return (strings in vector)
  [env {:keys [item-ids]}]
  (letfn [(f [result item-id]
             (conj result (delete-item-f env {:item-id item-id})))]
         (reduce f [] item-ids)))

(defmutation delete-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (?)
             [env mutation-props]
             {::pathom.co/op-name 'storage.media-lister/delete-items!}
             (delete-items-f env mutation-props))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
