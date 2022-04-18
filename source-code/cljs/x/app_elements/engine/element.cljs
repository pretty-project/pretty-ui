
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v1.0.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.css     :as css]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :refer [dissoc-in]]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- apply-dimension
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ; @param (keyword) dimension-key
  ; @param (keyword) dimension-data-key
  ; @param (keyword, px or string) dimension-value
  ;
  ; @example
  ;  (apply-dimension :my-element {...} :width :data-min-width 128)
  ;  =>
  ;  {:style {:width "128px"}}
  ;
  ; @example
  ;  (apply-dimension :my-element {...} :width :data-min-width "128px")
  ;  =>
  ;  {:style {:width "128px"}}
  ;
  ; @example
  ;  (apply-dimension :my-element {...} :width :data-min-width :s)
  ;  =>
  ;  {:data-min-width :s}
  ;
  ; @return (keyword or string)
  [element-props dimension-key dimension-data-key dimension-value]
  (cond (integer? dimension-value) (assoc-in element-props [:style dimension-key] (css/px dimension-value))
        (string?  dimension-value) (assoc-in element-props [:style dimension-key] (param  dimension-value))
        (keyword? dimension-value) (assoc    element-props dimension-data-key dimension-value)))

(defn element-id->extended-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (keyword) flag
  ;
  ; @example
  ;  (element/element-id->extended-id :my-namespace/my-element :popup)
  ;  =>
  ;  :my-namespace/my-element--popup
  ;
  ; @return (keyword)
  [element-id flag]
  (keyword/append element-id flag "--"))

(defn element-props->render-element-header?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)(opt)}
  ;
  ; @return (boolean)
  [{:keys [icon label]}]
  (or icon label))

(defn element-default-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:class (keyword or keywords in vector)(opt)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :id (string)
  ;   :key (string)}
  [element-id {:keys [class]}]
  ; BUG#4044
  ; Ha egy listában a listaelemek toggle elemet tartalmaznak és ...
  ; ... a toggle elem nem kap egyedi azonosítót, mert ugyanaz az azonosító ismétlődne
  ;     az összes listaelem toggle elemében,
  ; ... a toggle elem {:hover-color ...} tulajdonsággal rendelkezik,
  ; ... az element-default-attributes függvény React kulcsként alkalmazza az elemek
  ;     azonosítóját,
  ; ... az egyes listaelemekre kattintva olyan változás történik (pl. kijelölés),
  ;     ami miatt az adott listaelem paraméterezése megváltozik,
  ; akkor az egyes listaelemekre kattintva ...
  ; ... a megváltozó paraméterek miatt a listaelem újrarenderelődik,
  ; ... a listaelem toggle eleme is újrarenderelődik, ami miatt új azonosítót kap,
  ; ... a toggle elem az új azonosítója miatt úja React kulcsot kap,
  ; ... a toggle elem az új React kulcs beállításának pillanatában másik React-elemmé
  ;     változik és a váltás közben Ca. 15ms ideig nem látszódik a {:hover-color ...}
  ;     tulajdonság színe (rövid villanásnak tűnik)
  {:class (css/join-class :x-element class)
   :id    (a/dom-value element-id)})
  ;:key   (a/dom-value element-id)

(defn element-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:horizontal-align (keyword)(opt)
  ;    :center, :left, :right, :space-between
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :none, :right

  ; TEMP
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both
  ; TEMP

  ;   :layout (keyword)(opt)
  ;   :orientation (keyword)(opt)
  ;   :position (keyword)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :both, :horizontal, :none, :vertical
  ;   :vertical-align (keyword)(opt)
  ;    :bottom, :center, :space-between, :top
  ;   :vertical-align (keyword)(opt)
  ;    :center, :left, :none, :right}
  ;
  ; @return (map)
  ;  {:data-horizontal-align (keyword)
  ;   :data-horizontal-position (keyword)
  ;   :data-layout (keyword)
  ;   :data-orientation (keyword)
  ;   :data-position (keyword)
  ;   :data-stretch-orientation (keyword)
  ;   :data-vertical-align (keyword)
  ;   :data-vertical-position (keyword)}
  [_ {:keys [border-radius horizontal-align horizontal-position indent layout orientation position
             stretch-orientation vertical-align vertical-position]}]

  (cond-> {} horizontal-align    (assoc :data-horizontal-align    horizontal-align)
             horizontal-position (assoc :data-horizontal-position horizontal-position)

             ; TEMP
             (:bottom     indent) (assoc :data-indent-bottom     (:bottom     indent))
             (:left       indent) (assoc :data-indent-left       (:left       indent))
             (:right      indent) (assoc :data-indent-right      (:right      indent))
             (:top        indent) (assoc :data-indent-top        (:top        indent))
             (:horizontal indent) (assoc :data-indent-horizontal (:horizontal indent))
             (:vertical   indent) (assoc :data-indent-vertical   (:vertical   indent))
             (:all        indent) (assoc :data-indent-all        (:all        indent))

             border-radius (assoc :data-border-radius border-radius)
             ; TEMP

             layout              (assoc :data-layout              layout)
             orientation         (assoc :data-orientation         orientation)
             position            (assoc :data-position            position)
             stretch-orientation (assoc :data-stretch-orientation stretch-orientation)
             vertical-align      (assoc :data-vertical-align      vertical-align)
             vertical-position   (assoc :data-vertical-position   vertical-position)))

(defn element-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:background-color (keyword)(opt)
  ;   :border-color (keyword)(opt)
  ;   :color (keyword)(opt)
  ;   :font-size (keyword)(opt)
  ;   :font-weight (keyword)(opt)
  ;   :hover-color (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)}
  ;
  ; @return
  ;  {:data-color (keyword)
  ;   :data-font-size (keyword)
  ;   :data-font-weight (keyword)
  ;   :data-hover-color (keyword)
  ;   :data-icon-family (keyword)
  ;   :data-variant (keyword)
  ;   :style (map)
  ;    {:height (string or keyword)
  ;     :width (string or keyword)}}
  [_ {:keys [background-color border-color color font-size font-weight hover-color icon-family style variant]}]
  (cond-> {} background-color (assoc :data-background-color background-color)
             border-color     (assoc :data-border-color     border-color)
             color            (assoc :data-color            color)
             font-size        (assoc :data-font-size        font-size)
             font-weight      (assoc :data-font-weight      font-weight)
             hover-color      (assoc :data-hover-color      hover-color)
             icon-family      (assoc :data-icon-family      icon-family)
             style            (assoc :style                 style)
             variant          (assoc :data-variant          variant)))

(defn element-dimension-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:height (keyword, px or string)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   :size (keyword)(opt)
  ;   :width (keyword, px or string)(opt)}
  ;
  ; @return
  ;  {:data-height (keyword)
  ;   :data-min-width (keyword)
  ;   :data-size (keyword)
  ;   :data-width (keyword)
  ;   :style (map)
  ;    {:height (string)
  ;     :min-height (string)
  ;     :min-width (string)
  ;     :width (string)}}
  [_ {:keys [height min-height min-width size width]}]
  (cond-> {} height     (apply-dimension :height     :data-height     height)
             width      (apply-dimension :width      :data-width      width)
             min-height (apply-dimension :min-height :data-min-height min-height)
             min-width  (apply-dimension :min-width  :data-min-width  min-width)
             size       (assoc                       :data-size       size)))

(defn element-generic-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:alt (string)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :selectable? (boolean)(opt)
  ;   :src (string)(opt)}
  ;
  ; @return
  ;  {:alt (string)
  ;   :data-disabled (boolean)
  ;   :data-selectable (boolean)
  ;   :src (string)}
  [_ {:keys [alt disabled? hover-color selectable? src]}]
  (cond-> {} alt (assoc :alt alt)
             src (assoc :src src)
             (some? disabled?)   (assoc :data-disabled   disabled?)
             (some? selectable?) (assoc :data-selectable selectable?)
             (nil?  selectable?) (assoc :data-selectable false)
             ; XXX#4005
             ; A {:hover-color ...} tulajdonságként átadott szín használatához, minden esetben
             ; szükséges a {:data-disabled ...} attribútumot alkalmazni!
             hover-color (assoc :data-disabled (boolean disabled?))))

(defn element-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map)(opt) element-attributes
  ;
  ; @return (map)
  [element-id element-props & [element-attributes]]
  (merge (element-default-attributes   element-id element-props)
         (element-layout-attributes    element-id element-props)
         (element-style-attributes     element-id element-props)
         (element-dimension-attributes element-id element-props)
         (element-generic-attributes   element-id element-props)
         (param element-attributes)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (get-in db (db/path :elements/primary element-id)))

(defn get-element-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ;
  ; @usage
  ;  (r element/get-element-prop db :my-element :my-prop)
  ;
  ; @return (*)
  [db [_ element-id prop-key]]
  (let [element-props (r get-element-props db element-id)]
       (get element-props prop-key)))

(defn get-element-subprop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (vector) prop-path
  ;
  ; @usage
  ;  (r element/get-element-subprop db :my-element [:my-prop :my-subprop])
  ;
  ; @return (*)
  [db [_ element-id prop-path]]
  (let [element-props (r get-element-props db element-id)]
       (get-in element-props prop-path)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------



(defn store-element-props!
  [db [_ element-id element-props]]
  (assoc-in db [:elements/primary :data-items element-id] element-props))

(defn remove-element-props!
  [db [_ element-id]]
  (dissoc-in db [:elements/primary :data-items element-id]))



(defn set-element-prop!
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ; @param (*) prop-value
  ;
  ; @usage
  ;  (r element/set-element-prop! db :my-element :my-prop "My value")
  ;
  ; @return (map)
  [db [_ element-id prop-key prop-value]]
  (assoc-in db (db/path :elements/primary element-id prop-key) prop-value))

; @usage
;  [:elements/set-element-prop! :my-element :my-prop "My value"]
(a/reg-event-db :elements/set-element-prop! set-element-prop!)

(defn update-element-prop!
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ; @param (*) prop-value
  ;
  ; @usage
  ;  (r element/update-element-prop! db :my-element :my-prop vector/conj-item "My value" "Your value")
  ;
  ; @return (map)
  [db [_ element-id prop-key f & params]]
  (let [value         (r get-element-prop db element-id prop-key)
        updated-value (apply f value params)]
       (r set-element-prop! db element-id prop-key updated-value)))

(defn set-element-subprop!
  ; @param (keyword) element-id
  ; @param (vector) prop-path
  ; @param (*) prop-value
  ;
  ; @usage
  ;  (r element/set-element-subprop! db :my-element [:my-prop :my-subprop] "My value")
  ;
  ; @return (map)
  [db [_ element-id prop-path prop-value]]
  (let [prop-path (vector/concat-items (db/path :elements/primary element-id)
                                       (param prop-path))]
       (assoc-in db prop-path prop-value)))

; @usage
;  [:elements/set-element-subprop! :my-element [:my-prop :my-subprop] "My value"]
(a/reg-event-db :elements/set-element-subprop! set-element-subprop!)

(defn remove-element-prop!
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ;
  ; @usage
  ;  (r element/remove-element-prop! db :my-element :my-prop)
  ;
  ; @return (map)
  [db [_ element-id prop-key]]
  (dissoc-in db (db/path :elements/primary element-id prop-key)))

; @usage
;  [:elements/remove-element-prop! :my-element :my-prop]
(a/reg-event-db :elements/remove-element-prop! remove-element-prop!)

(defn remove-element-subprop!
  ; @param (keyword) element-id
  ; @param (vector) prop-path
  ;
  ; @usage
  ;  (r element/remove-element-subprop! db :my-element [:my-prop :my-subprop])
  ;
  ; @return (map)
  [db [_ element-id prop-path]]
  (let [prop-path (vector/concat-items (db/path :elements/primary element-id)
                                       (param prop-path))]
       (dissoc-in db prop-path)))

; @usage
;  [:elements/remove-element-subprop! :my-element [:my-prop :my-subprop]]
(a/reg-event-db :elements/remove-element-subprop! remove-element-subprop!)
