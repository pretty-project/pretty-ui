
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.views
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-layouts.api :as layouts]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; @param (keyword) extension-id
  ; @param (map) body-props
  ;  {:body (metamorphic-content)}
  ;
  ; @usage
  ;  [view-selector/body :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-body [extension-id] [:div ...])
  ;  [view-selector/body :my-extension {:body #'my-body}]
  [extension-id {:keys [body]}]
  [body extension-id])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; @param (keyword) extension-id
  ; @param (map) header-props
  ;  {:header (metamorphic-content)}
  ;
  ; @usage
  ;  [view-selector/header :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-header [extension-id] [:div ...])
  ;  [view-selector/header :my-extension {:header #'my-header}]
  [extension-id {:keys [header]}]
  [header extension-id])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) extension-id
  ; @param (map) view-props
  ;  {:body (metamorphic-content)
  ;   :header (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [view-selector/view :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-header [extension-id] [:div ...])
  ;  (defn my-body   [extension-id] [:div ...])
  ;  [view-selector/view :my-extension {:body   #'my-body
  ;                                     :header #'my-header}]
  [extension-id view-props]
  (if (:header view-props) [layouts/layout-a extension-id {:body   [body   extension-id view-props]
                                                           :header [header extension-id view-props]}]
                           [layouts/layout-a extension-id {:body   [body   extension-id view-props]}]))
