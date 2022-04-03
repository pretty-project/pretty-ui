
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.color-stamp
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- stamp-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) stamp-props
  ;
  ; @return (map)
  ;  {:size (keyword)}
  [stamp-props]
  (merge {:size :s}
         (param stamp-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-stamp-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {:colors (strings in vector)(opt)}
  [_ {:keys [colors] :as stamp-props}]
  (if (vector/nonempty? colors)
      (letfn [(f [color-stamp color] (conj color-stamp [:div.x-color-stamp--color {:style {:background-color color}}]))]
             (reduce f [:<>] colors))
      [:div.x-color-stamp--placeholder]))

(defn toggle-color-stamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {:on-click (metamorphic-event)(opt)}
  [stamp-id {:keys [on-click] :as stamp-props}]
  [:button.x-color-stamp (engine/element-attributes stamp-id stamp-props
                                                    {:on-click    #(a/dispatch on-click)
                                                     :on-mouse-up #(environment/blur-element!)})
                         [color-stamp-colors stamp-id stamp-props]])

(defn static-color-stamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  [stamp-id stamp-props]
  [:div.x-color-stamp (engine/element-attributes stamp-id stamp-props)
                      [color-stamp-colors        stamp-id stamp-props]])

(defn- color-stamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stamp-id
  ; @param (map) stamp-props
  ;  {:on-click (metamorphic-event)(opt)}
  [stamp-id {:keys [on-click] :as stamp-props}]
  (if on-click [toggle-color-stamp stamp-id stamp-props]
               [static-color-stamp stamp-id stamp-props]))

(defn element
  ; @param (keyword)(opt) stamp-id
  ; @param (map) stamp-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :colors (strings in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :on-click (metamorphic-event)(opt)
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/color-stamp {...}]
  ;
  ; @usage
  ;  [elements/color-stamp :my-color-stamp {...}]
  ;
  ; @usage
  ;  [elements/color-stamp :my-color-stamp {:colors ["red" "green" "blue"]}]
  ([stamp-props]
   [element (a/id) stamp-props])

  ([stamp-id stamp-props]
   (let [stamp-props (stamp-props-prototype stamp-props)]
        [color-stamp stamp-id stamp-props])))
