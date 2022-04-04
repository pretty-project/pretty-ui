
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.thumbnail
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ;  {:size (keyword)}
  [thumbnail-props]
  (merge {:size :s}
         (param thumbnail-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:on-click (metamorphic-event)(opt)
  ;   :uri (string)(opt)}
  [thumbnail-id {:keys [on-click uri] :as thumbnail-props}]
  [:button.x-thumbnail (engine/element-attributes thumbnail-id thumbnail-props
                                                  {:on-click    #(a/dispatch on-click)
                                                   :on-mouse-up #(environment/blur-element!)
                                                   :style {:background-image (css/url uri)}})])

(defn- static-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:uri (string)(opt)}
  [thumbnail-id {:keys [uri] :as thumbnail-props}]
  [:div.x-thumbnail (engine/element-attributes thumbnail-id thumbnail-props
                                               {:style {:background-image (css/url uri)}})])

(defn- thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:on-click (metamorphic-event)(opt)}
  [thumbnail-id {:keys [on-click] :as thumbnail-props}]
  (cond (some? on-click) [toggle-thumbnail thumbnail-id thumbnail-props]
        (nil?  on-click) [static-thumbnail thumbnail-id thumbnail-props]))

(defn element
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :on-click (metamorphic-event)(opt)
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :style (map)(opt)
  ;   :uri (string)(opt)}
  ;
  ; @usage
  ;  [elements/thumbnail {...}]
  ;
  ; @usage
  ;  [elements/thumbnail :my-thumbnail {...}]
  ([thumbnail-props]
   [element (a/id) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   (let [thumbnail-props (thumbnail-props-prototype thumbnail-props)]
        [thumbnail thumbnail-id thumbnail-props])))
