
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.toggle
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
  ;
  ; @return (map)
  ;  {:layout (keyword)}
  [toggle-props]
  (merge {:layout :fit}
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
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :hover-color (keyword)(opt)
  ;    :highlight, :muted
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
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
