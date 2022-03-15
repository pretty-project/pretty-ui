
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.row
    (:require [mid-fruits.candy                    :refer [param]]
              [x.app-components.api                :as components]
              [x.app-core.api                      :as a]
              [x.app-elements.engine.api           :as engine]
              [x.app-elements.flex-handler.helpers :as flex-handler.helpers]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) row-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :stretch-orientation (keyword)
  ;   :vertical-align (keyword)
  ;   :wrap-items? (boolean)}
  [row-props]
  (merge {:horizontal-align    :left
          :stretch-orientation :horizontal
          :vertical-align      :center
          :wrap-items?         true}
         (param row-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:content (metamorphic-content)(opt)}
  [row-id {:keys [content]}]
  [:div.x-row--body [components/content row-id content]])

(defn- row
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:content (metamorphic-content)(opt)}
  [row-id row-props]
  [:div.x-row (flex-handler.helpers/flexible-attributes row-id row-props)
              [row-body                                 row-id row-props]])

(defn element
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right, :space-around, :space-between, :space-evenly
  ;    Default: :left
  ;   :style (map)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :horizontal
  ;   :vertical-align (keyword)(opt)
  ;    :top, :center, :bottom
  ;    Default: :center
  ;   :wrap-items? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  [elements/row {...}]
  ;
  ; @usage
  ;  [elements/row :my-row {...}]
  ([row-props]
   [element (a/id) row-props])

  ([row-id row-props]
   (let [row-props (row-props-prototype row-props)]
        [row row-id row-props])))
