
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.27
; Description:
; Version: v0.3.8
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.views
    (:require [x.app-core.api       :as a :refer [r]]
              [x.app-components.api :as components]
              [x.app-layouts.api    :as layouts]))



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
  ;  (defn my-body [extension-id {:keys [view-id] :as body-props}] [:div ...])
  ;  [view-selector/body :my-extension {:body #'my-body}]
  ;
  ; @return (component)
  [extension-id {:keys [body] :as body-props}]
  [components/subscriber extension-id
                         {:base-props body-props
                          :render-f    body
                          :subscriber  [:view-selector/get-view-props extension-id]}])



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
  ;  (defn my-header [extension-id {:keys [view-id] :as header-props}] [:div ...])
  ;  [view-selector/header :my-extension {:header #'my-header}]
  ;
  ; @return (component)
  [extension-id {:keys [header] :as body-props}]
  [components/subscriber extension-id
                         {:base-props body-props
                          :render-f   header
                          :subscriber [:view-selector/get-view-props extension-id]}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) view-props
  ;  {:body (metamorphic-content)
  ;   :header (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [extension-id {:keys [body header] :as view-props}]
  (if header [layouts/layout-a extension-id {:body   [body   extension-id view-props]
                                             :header [header extension-id view-props]}]
             [layouts/layout-a extension-id {:body   [body   extension-id view-props]}]))

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
  ;  (defn my-header [extension-id {:keys [view-id] :as header-props}] [:div ...])
  ;  (defn my-body   [extension-id {:keys [view-id] :as body-props}]   [:div ...])
  ;  [view-selector/view :my-extension {:body   #'my-body
  ;                                     :header #'my-header}]
  ;
  ; @return (component)
  [extension-id view-props]
  [components/subscriber extension-id
                         {:base-props view-props
                          :render-f   view-structure
                          :subscriber [:view-selector/get-view-props extension-id]}])
