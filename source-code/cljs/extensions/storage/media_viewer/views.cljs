
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-viewer.views
    (:require [layouts.popup-b.api :as popup-b]
              [mid-fruits.io       :as io]
              [x.app-core.api      :as a]
              [x.app-elements.api  :as elements]
              [x.app-media.api     :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn close-icon-button
  [viewer-id]
  [elements/icon-button ::close-icon-button
                        {:color    :invert
                         :keypress {:key-code 27}
                         :on-click [:ui/close-popup! :storage.media-viewer/view]
                         :preset   :close
                         :style    {:position :fixed :right 0 :top :0}}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pdf-item
  [viewer-id]
  (let [% @(a/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       [:div.storage--media-viewer--pdf-item
         [:iframe.storage--media-viewer--pdf {:src (-> % :item-filename media/filename->media-storage-uri)}]]))

(defn image-item
  [viewer-id]
  (let [% @(a/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       [:div.storage--media-viewer--image-item
         [:div.storage--media-viewer--icon  [elements/icon {:icon :insert_drive_file :color :invert}]]
         [:img.storage--media-viewer--image {:src (-> % :item-filename media/filename->media-storage-uri)}]]))

(defn media-item
  [viewer-id]
  (let [% @(a/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       (case (-> % :item-filename io/filename->mime-type)
             "application/pdf" [pdf-item   viewer-id]
                               [image-item viewer-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  [viewer-id]
  [:<> [media-item        viewer-id]
       [close-icon-button viewer-id]])

(defn view
  [viewer-id]
  [popup-b/layout :storage.media-viewer/view
                  {:content [view-structure viewer-id]}])
