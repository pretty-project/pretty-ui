
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.text
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) text-props
  ;
  ; @return (map)
  ;  {:font-size (keyword)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :selectable? (boolean)}
  [text-props]
  (merge {:font-size        :s
          :font-weight      :normal
          :horizontal-align :left
          :layout           :row
          :selectable?      true}
         (param text-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {:content (metamorphic-content)}
  [text-id {:keys [content] :as text-props}]
  [:div.x-text (engine/element-attributes text-id text-props)
               [:div.x-text--body [components/content text-id content]]])

(defn element
  ; XXX#0439
  ; A text elemen megjelenített szöveg megtörik, ha nincs elegendő hely.
  ; A label elemen megjelenített szöveg nem törik meg akkor sem, ha nincs elegendő hely.
  ;
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :color (keyword)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :content (metamorphic-content)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :bold, :extra-bold, :normal
  ;    Default: :normal
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :left
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :selectable? (boolean)(opt)
  ;    Default: true
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/text {...}]
  ;
  ; @usage
  ;  [elements/text :my-text {...}]
  ([text-props]
   [element (a/id) text-props])

  ([text-id text-props]
   (let [text-props (text-props-prototype text-props)]
        [text text-id text-props])))
