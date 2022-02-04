
(ns app-extensions.storage.media-viewer.views
    (:require [x.app-core.api :as a]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id])



;; -- Element components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) viewer-id
  ; @param (map) viewer-props
  ;
  ; @usage
  ;  [storage/media-viewer {...}]
  ;
  ; @usage
  ;  [storage/media-viewer :my-viewer {...}]
  ;
  ; @return (component)
  ([viewer-props]
   [element (a/id) viewer-props])

  ([viewer-id viewer-props]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-viewer/render-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  (fn [_ [_ extension-id]]
      [:ui/add-popup! :storage.media-viewer/view
                      {:body   [body extension-id]
                       :layout :unboxed}]))
