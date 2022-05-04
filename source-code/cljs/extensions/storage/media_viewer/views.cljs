
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-viewer.views
    (:require [mid-fruits.io      :as io]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-media.api    :as media]
              [x.app-ui.api       :as ui]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [viewer-id])
  ;[ui/close-popup-header :storage.media-viewer/view {}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- pdf-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [viewer-id]
  (let [% @(a/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       [:div.storage--media-viewer--pdf-item
         [:iframe.storage--media-viewer--pdf {:src (-> % :item-filename media/filename->media-storage-uri)}]]))

(defn- image-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [viewer-id]
  (let [% @(a/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       [:div.storage--media-viewer--image-item
         [:div.storage--media-viewer--icon  [elements/icon {:icon :insert_drive_file :color :invert}]]
         [:img.storage--media-viewer--image {:src (-> % :item-filename media/filename->media-storage-uri)}]]))

(defn- media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [viewer-id]
  (let [% @(a/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       (case (-> % :item-filename io/filename->mime-type)
             "application/pdf" [pdf-item   viewer-id]
                               [image-item viewer-id])))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [viewer-id]
  [:<> [media-item viewer-id]])



;; -- Element components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) viewer-id
  ; @param (map) viewer-props
  ;  {:directory-id (string)}
  ;
  ; @usage
  ;  [storage/media-viewer {...}]
  ;
  ; @usage
  ;  [storage/media-viewer :my-viewer {...}]
  ;
  ; @usage
  ;  [storage/media-viewer :my-viewer {:directory-id "my-directory"}]
  ([viewer-props]
   [element (a/id) viewer-props])

  ([viewer-id viewer-props]))
