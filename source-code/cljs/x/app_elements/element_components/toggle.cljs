
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.toggle
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) toggle-props
  ;  {:background-color (keyword)(opt)
  ;   :hover-color (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:border-radius (keyword)
  ;   :layout (keyword)}
  [{:keys [background-color hover-color] :as toggle-props}]
  (merge {:layout :fit}
         ; TEMP
         ; A border-radius tulajdonság nem műküdik még! (css)
         (if background-color {:border-radius :s})
         (if hover-color      {:border-radius :s})
         (param toggle-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;  {:content (metamorphic-content)(opt)}
  [toggle-id {:keys [content] :as toggle-props}]
  [:button.x-toggle--body (engine/clickable-body-attributes toggle-id toggle-props)
                          [components/content               toggle-id content]])

(defn- toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  [toggle-id toggle-props]
  [:div.x-toggle (engine/element-attributes toggle-id toggle-props)
                 [toggle-body               toggle-id toggle-props]])

(defn element
  ; @param (keyword)(opt) toggle-id
  ; @param (map) toggle-props
  ;  {:border-radius (keyword)(opt)
  ;   :border-radius (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :hover-color (keyword)(opt)
  ;    :highlight, :muted, :none
  ;    Default: :none
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :fit
  ;   :on-click (metamorphic-event)
  ;   :on-right-click (metamorphic-event)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/toggle {...}]
  ;
  ; @usage
  ;  [elements/toggle :my-toggle {...}]
  ([toggle-props]
   [element (a/id) toggle-props])

  ([toggle-id toggle-props]
   (let [toggle-props (toggle-props-prototype toggle-props)]
        [toggle toggle-id toggle-props])))
