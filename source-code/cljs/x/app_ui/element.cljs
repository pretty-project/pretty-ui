
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.15
; Description:
; Version: v0.6.8
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.element
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.map       :as map]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-ui.engine      :as engine]))



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
  ;  "x-app-my-renderer--element"
  ;
  ; @return (string)
  [renderer-id]
  (let [dom-id (engine/renderer-id->dom-id renderer-id)]
       (a/dom-value dom-id "element")))

(defn- element-additional-class
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (string) flag
  ;
  ; @example
  ;  (element-additional-class :my-renderer "label")
  ;  =>
  ;  "x-app-my-renderer--element--label"
  ;
  ; @return (string)
  [renderer-id flag]
  (str (element-class renderer-id) "--" flag))

(defn element-default-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  ;  {:class (strings in vector)
  ;   :id (string)
  ;   :key (string)}
  [renderer-id element-id _]
  {:class [(element-class renderer-id)]
   :id    (a/dom-value element-id)
   :key   (a/dom-value element-id)})

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
  ;   :position (keyword)(opt)
  ;   :size (keyword)(opt)
  ;   :trim-content? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-horizontal-align (string)
  ;   :data-layout (string)
  ;   :data-min-height (string)
  ;   :data-min-width (string)
  ;   :data-position (string)
  ;   :data-size (string)
  ;   :data-trim-content (boolean)}
  [_ _ {:keys [horizontal-align layout min-height min-width minimized? position size trim-content?]}]
  (cond-> {} (some? layout)           (assoc :data-layout           (a/dom-value layout))
             (some? horizontal-align) (assoc :data-horizontal-align (a/dom-value horizontal-align))
             (some? min-height)       (assoc :data-min-height       (a/dom-value min-height))
             (some? min-width)        (assoc :data-min-width        (a/dom-value min-width))
             (some? position)         (assoc :data-position         (a/dom-value position))
             (some? size)             (assoc :data-size             (a/dom-value size))
             (some? trim-content?)    (assoc :data-trim-content     (boolean     trim-content?))))

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
  ;  {:data-animation (string)
  ;   :data-color (string)}
  [_ _ {:keys [color reveal-animated?]}]
  (cond-> {} (some?   color)            (assoc :data-color (a/dom-value color))
             (boolean reveal-animated?) (assoc :data-animation "reveal")))

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



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:content (metamorphic-content)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :subscriber (subscription vector)(opt)
  ;    XXX#8711}
  ;
  ; @return (component)
  [element-id element-props]
  (let [context-props (components/extended-props->content-props element-props)]
       [components/content element-id context-props]))
