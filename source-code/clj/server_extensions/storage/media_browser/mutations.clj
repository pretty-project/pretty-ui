
(ns server-extensions.storage.media-browser.mutations
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [server-extensions.storage.engine      :as engine]
              [server-plugins.item-browser.api       :as item-browser]))



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
             {::pathom.co/op-name 'storage.media-browser/create-directory!}
             (create-directory-f env mutation-props))

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
  (reduce #(conj %1 (delete-item-f env {:item-id %2})) [] item-ids))

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
(def HANDLERS [create-directory! delete-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
