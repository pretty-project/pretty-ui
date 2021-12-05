
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.9.0
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.css     :as css]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map :refer [dissoc-in]]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-sync.api     :as sync]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (boolean (or (some? icon)
               (some? label))))

(defn element-props-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (vector)
  [element-id]
  (db/path ::elements element-id))

(defn element-prop-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (keyword) prop-id
  ;
  ; @return (vector)
  [element-id prop-id]
  (db/path ::elements element-id prop-id))

(defn element-default-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  ;  {:id (string)
  ;   :key (string)}
  [element-id _]
  {:id  (a/dom-value element-id)
   :key (a/dom-value element-id)})

(defn element-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:horizontal-align (keyword)(opt)
  ;    :left, :center, :right, :space-between
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both
  ;   :layout (keyword)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   :orientation (keyword)(opt)
  ;   :position (keyword)(opt)
  ;   :size (keyword)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;   :vertical-align (keyword)(opt)
  ;    :top, :center, :bottom, :space-between}
  ;
  ; @return (map)
  ;  {:data-horizontal-align (keyword)
  ;   :data-layout (keyword)
  ;   :data-min-width (keyword)
  ;   :data-orientation (keyword)
  ;   :data-position (keyword)
  ;   :data-size (keyword)
  ;   :data-stretch-orientation (keyword)
  ;   :data-vertical-align (keyword)}
  [_ {:keys [horizontal-align indent layout min-width orientation position size stretch-orientation
             vertical-align]}]
  (cond-> {} (some? horizontal-align)    (assoc :data-horizontal-align    horizontal-align)
             (some? indent)              (assoc :data-indent              indent)
             (some? layout)              (assoc :data-layout              layout)
             (some? min-width)           (assoc :data-min-width           min-width)
             (some? orientation)         (assoc :data-orientation         orientation)
             (some? position)            (assoc :data-position            position)
             (some? size)                (assoc :data-size                size)
             (some? stretch-orientation) (assoc :data-stretch-orientation stretch-orientation)
             (some? vertical-align)      (assoc :data-vertical-align      vertical-align)))

(defn element-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:background-color (keyword)(opt)
  ;   :border-color (keyword)(opt)
  ;   :class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;   :font-size (keyword)(opt)
  ;   :font-weight (keyword)(opt)
  ;   :height (keyword, px or string)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)
  ;   :width (keyword, px or string)(opt)}
  ;
  ; @return
  ;  {:class (string or vector)
  ;   :data-color (keyword)
  ;   :data-font-size (keyword)
  ;   :data-font-weight (keyword)
  ;   :data-height (keyword)
  ;   :data-icon-family (keyword)
  ;   :data-variant (keyword)
  ;   :style (map)
  ;    {:height (string or keyword)
  ;     :width (string or keyword)}
  ;   :data-width (keyword)}
  [_ {:keys [class background-color border-color color font-size font-weight height
             icon-family style variant width]}]
  (cond-> {} (some?    background-color) (assoc :data-background-color (param  background-color))
             (some?    border-color)     (assoc :data-border-color     (param  border-color))
             (some?    color)            (assoc :data-color            (param  color))
             (some?    font-size)        (assoc :data-font-size        (param  font-size))
             (some?    font-weight)      (assoc :data-font-weight      (param  font-weight))
             (some?    icon-family)      (assoc :data-icon-family      (param  icon-family))
             (some?    style)            (assoc :style                 (param  style))
             (integer? height)           (assoc-in [:style :height]    (css/px height))
             (string?  height)           (assoc-in [:style :height]    (param  height))
             (keyword? height)           (assoc :data-height           (param  height))
             (integer? width)            (assoc-in [:style :width]     (css/px width))
             (string?  width)            (assoc-in [:style :width]     (param  width))
             (keyword? width)            (assoc :data-width            (param  width))
             (some?    variant)          (assoc :data-variant          (param  variant))
             :use-class                  (assoc :class                 (css/join-class "x-element" class))))

(defn element-generic-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:alt (string)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :highlighted? (boolean)(opt)
  ;   :selectable? (boolean)(opt)
  ;   :src (string)(opt)}
  ;
  ; @return
  ;  {:alt (string)
  ;   :data-disabled (boolean)
  ;   :data-highlighted (boolean)
  ;   :data-selectable (boolean)
  ;   :src (string)}
  [_ {:keys [alt disabled? highlighted? selectable? src]}]
  (cond-> {} (some?    alt)          (assoc :alt alt)
             (some?    src)          (assoc :src src)
             (boolean? disabled?)    (assoc :data-disabled    disabled?)
             (boolean? highlighted?) (assoc :data-highlighted highlighted?)
             (boolean? selectable?)  (assoc :data-selectable  selectable?)))

(defn element-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map)(opt) element-attributes
  ;
  ; @return (map)
  [element-id element-props & [element-attributes]]
  (merge (element-default-attributes element-id element-props)
         (element-layout-attributes  element-id element-props)
         (element-style-attributes   element-id element-props)
         (element-generic-attributes element-id element-props)
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
  (get-in db (db/path ::elements element-id)))

(defn get-element-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (keyword) prop-id
  ;
  ; @usage
  ;  (r element/get-element-prop db :my-element :my-prop)
  ;
  ; @return (*)
  [db [_ element-id prop-id]]
  (let [element-props (r get-element-props db element-id)]
       (get element-props prop-id)))

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

(defn get-element-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r get-element-props db element-id))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-prop!
  ; @param (keyword) element-id
  ; @param (keyword) prop-id
  ; @param (*) prop-value
  ;
  ; @usage
  ;  (r element/set-element-prop! db :my-element :my-prop "My value")
  ;
  ; @return (map)
  [db [_ element-id prop-id prop-value]]
  (assoc-in db (db/path ::elements element-id prop-id) prop-value))

; @usage
;  [:elements/set-element-prop! :my-element :my-prop "My value"]
(a/reg-event-db :elements/set-element-prop! set-element-prop!)

(defn update-element-prop!
  ; @param (keyword) element-id
  ; @param (keyword) prop-id
  ; @param (*) prop-value
  ;
  ; @usage
  ;  (r element/update-element-prop! db :my-element :my-prop vector/conj-item "My value" "Your value")
  ;
  ; @return (map)
  [db [_ element-id prop-id f & params]]
  (let [value         (r get-element-prop db element-id prop-id)
        updated-value (apply f value params)]
       (r set-element-prop! db element-id prop-id updated-value)))

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
  (let [prop-path (vector/concat-items (db/path ::elements element-id)
                                       (param prop-path))]
       (assoc-in db prop-path prop-value)))

; @usage
;  [:elements/set-element-subprop! :my-element [:my-prop :my-subprop] "My value"]
(a/reg-event-db :elements/set-element-subprop! set-element-subprop!)

(defn remove-element-prop!
  ; @param (keyword) element-id
  ; @param (keyword) prop-id
  ;
  ; @usage
  ;  (r element/remove-element-prop! db :my-element :my-prop)
  ;
  ; @return (map)
  [db [_ element-id prop-id]]
  (dissoc-in db (db/path ::elements element-id prop-id)))

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
  (let [prop-path (vector/concat-items (db/path ::elements element-id)
                                       (param prop-path))]
       (dissoc-in db prop-path)))

; @usage
;  [:elements/remove-element-subprop! :my-element [:my-prop :my-subprop]]
(a/reg-event-db :elements/remove-element-subprop! remove-element-subprop!)
