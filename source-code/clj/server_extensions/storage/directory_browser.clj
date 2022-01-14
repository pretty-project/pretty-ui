
(ns server-extensions.storage.directory-browser
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [prototypes.api    :as prototypes]
              [x.server-core.api :as a]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-plugins.item-browser.api       :as item-browser]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-directory-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) directory-item
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-item]
  (mongo-db/insert-document! "directories" directory-item
                             {:prototype-f #(prototypes/added-document-prototype request :directory %)}))

(defmutation add-directory-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) directory-item
             ;
             ; @return (namespaced map)
             [env directory-item]
             {::pathom.co/op-name 'storage/add-directory-item!}
             (add-directory-item-f env directory-item))

(defn add-file-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) file-item
  ;
  ; @return (namespaced map)
  [{:keys [request]} file-item]
  (mongo-db/insert-document! "files" file-item
                             {:prototype-f #(prototypes/added-document-prototype request :file %)}))

(defmutation add-file-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) file-item
             ;
             ; @return (namespaced map)
             [env file-item]
             {::pathom.co/op-name 'storage/add-file-item!}
             (add-file-item-f env file-item))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [add-directory-item! add-file-item!])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:item-browser/initialize! :storage :directory {}]})
