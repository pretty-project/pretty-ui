
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.label
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) label-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :horizontal-align (keyword)}
  [{:keys [icon] :as label-props}]
  (merge {:color            :default
          :font-size        :s
          :font-weight      :bold
          :horizontal-align :left}
         (if icon {:icon-family :material-icons-filled})
         (param label-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)}
  [_ {:keys [icon icon-family]}]
  [:i.x-label--icon {:data-icon-family icon-family}
                    (param icon)])

(defn- label-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:content (metamorphic-content)
  ;   :placeholder (metamorphic-content)(opt)}
  [label-id {:keys [content placeholder]}]
  ; XXX#9811
  ; Egyes esetekben a megjelenített szöveg értéke egy üres string, amíg a tényleges érték, nem
  ; töltődik le vagy nem töltődik be.
  ; Ilyenkor ha nem lenne minden esetben placeholder alkalmazva, akkor 0px magasságú lenne
  ; a label elem a letöltődés/betöltődés idejére, ami az alatta megjelenített tartalom
  ; esetleges ugrását okozná (a szöveg tényleges megjelenésekor)!
  (let [content (components/content label-id content)]
       (if (empty? content)
           (if placeholder [:div.x-label--placeholder (components/content label-id placeholder)]
                           [:div.x-label--placeholder "\u00A0"])
           [:div.x-label--body content])))

(defn- label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:icon (keyword)}
  [label-id {:keys [icon] :as label-props}]
  [:div.x-label (engine/element-attributes label-id label-props)
                (if icon [label-icon label-id label-props])
                [label-body label-id label-props]
                (if icon [:div.x-label--icon-placeholder])])

(defn element
  ; XXX#0439
  ; A label elemen megjelenített szöveg nem törik meg akkor sem, ha nincs elegendő hely.
  ; A text elemen megjelenített szöveg megtörik, ha nincs elegendő hely.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :color (keyword)(opt)
  ;    :default, :highlight, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :normal, :bold, extra-bold
  ;    Default :bold
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :left
  ;   :horizontal-position (keyword)(opt)
  ;    :center, :left, :none, :right
  ;    Default: :none
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :placeholder (metamorphic-content)(opt)
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :vertical-position (keyword)(opt)
  ;    :bottom, :center, :none, :top
  ;    Default: :none}
  ;
  ; @usage
  ;  [elements/label {...}]
  ;
  ; @usage
  ;  [elements/label :my-label {...}]
  ([label-props]
   [element (a/id) label-props])

  ([label-id label-props]
   (let [label-props (label-props-prototype label-props)]
        [label label-id label-props])))
