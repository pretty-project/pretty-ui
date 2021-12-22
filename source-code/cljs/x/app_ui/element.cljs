
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.15
; Description:
; Version: v0.7.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.element
    (:require [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a]
              [x.app-ui.engine  :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-class
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @example
  ;  (element-class :my-renderer)
  ;  =>
  ;  :x-app-my-renderer--element
  ;
  ; @return (keyword)
  [renderer-id]
  (keyword (str "x-app-" (name renderer-id) "--element")))

(defn- element-additional-class
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (string) flag
  ;
  ; @example
  ;  (element-additional-class :my-renderer "label")
  ;  =>
  ;  :x-app-my-renderer--element--label
  ;
  ; @return (keyword)
  [renderer-id flag]
  (keyword (str "x-app-" (name renderer-id) "--element--" flag)))

(defn element-default-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  ;  {:class (keyword)
  ;   :id (string)
  ;   :key (string)}
  [renderer-id element-id _]
  {:class (element-class renderer-id)
   :id    (a/dom-value   element-id)
   :key   (a/dom-value   element-id)})

(defn element-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:horizontal-align (keyword)(opt)
  ;   :layout (keyword)(opt)
  ;   :min-height (keyword)(opt)
  ;   :min-width (keyword)(opt)
  ;   :size (keyword)(opt)
  ;   :trim-content? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-horizontal-align (keyword)
  ;   :data-layout (keyword)
  ;   :data-min-height (keyword)
  ;   :data-min-width (keyword)
  ;   :data-size (keyword)
  ;   :data-trim-content (boolean)}
  [_ _ {:keys [horizontal-align layout min-height min-width minimized? size trim-content?]}]
  (cond-> {} (some? layout)           (assoc :data-layout           layout)
             (some? horizontal-align) (assoc :data-horizontal-align horizontal-align)
             (some? min-height)       (assoc :data-min-height       min-height)
             (some? min-width)        (assoc :data-min-width        min-width)
             (some? size)             (assoc :data-size             size)
             (boolean? trim-content?) (assoc :data-trim-content     trim-content?)))

(defn element-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:color (keyword)(opt)
  ;   :reveal-animated? (boolean)}
  ;
  ; @return
  ;  {:data-animation (keyword)
  ;   :data-color (keyword)}
  [_ _ {:keys [color reveal-animated?]}]
  (cond-> {} (some?   color)            (assoc :data-color color)
             (boolean reveal-animated?) (assoc :data-animation :reveal)))

(defn element-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map)(opt) element-attributes
  ;
  ; @return (map)
  [renderer-id element-id element-props & [element-attributes]]
  (merge (element-default-attributes renderer-id element-id element-props)
         (element-layout-attributes  renderer-id element-id element-props)
         (element-style-attributes   renderer-id element-id element-props)
         (param element-attributes)))
