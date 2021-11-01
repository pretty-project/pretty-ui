
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.8.6
; Compatibility: x4.3.9



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



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-props->element-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:selected? (boolean)(opt)}
  ;
  ; @return (boolean)
  [{:keys [selected?]}]
  (boolean selected?))

(defn element-id->extended-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (keyword) flag
  ;
  ; @example
  ;  (element/element-id->extended-id :namespace/my-element :popup)
  ;  => :namespace/my-element--popup
  ;
  ; @return (keyword)
  [element-id flag]
  (keyword/append element-id flag "--"))

(defn element-props->render-element-header?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;  {:expandable? (boolean)(opt)
  ;   :icon (keyword)(opt) Material icon class
  ;   :label (metamorphic-content)(opt)}
  ;
  ; @return (boolean)
  [{:keys [expandable? icon label]}]
  (boolean (or (some?   icon)
               (some?   label)
               (boolean expandable?))))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  {:id  (keyword/to-dom-value element-id)
   :key (keyword/to-dom-value element-id)})

(defn element-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:ghost-view? (boolean)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right, :space-between
  ;   :icon-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   :layout (keyword)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   :orientation (keyword)(opt)
  ;   :position (keyword)(opt)
  ;   :size (keyword)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;   :vertical-align (keyword)(opt)
  ;    :top, :center, :bottom, :space-between
  ;   :wrap-items? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-ghost-view (boolean)
  ;   :data-horizontal-align (string)
  ;   :data-icon-size (string)
  ;   :data-layout (string)
  ;   :data-min-width (string)
  ;   :data-orientation (string)
  ;   :data-position (string)
  ;   :data-size (string)
  ;   :data-stretch-orientation (string)
  ;   :data-vertical-align (string)
  ;   :data-wrap-items (string)}
  [_ {:keys [ghost-view? horizontal-align icon-size layout min-width orientation position
             size stretch-orientation vertical-align wrap-items?]}]
  (cond-> (param {})
          (some? ghost-view?)
          (assoc :data-ghost-view          (boolean              ghost-view?))
          (some? horizontal-align)
          (assoc :data-horizontal-align    (keyword/to-dom-value horizontal-align))
          (some? icon-size)
          (assoc :data-icon-size           (keyword/to-dom-value icon-size))
          (some? layout)
          (assoc :data-layout              (keyword/to-dom-value layout))
          (some? min-width)
          (assoc :data-min-width           (keyword/to-dom-value min-width))
          (some? orientation)
          (assoc :data-orientation         (keyword/to-dom-value orientation))
          (some? position)
          (assoc :data-position            (keyword/to-dom-value position))
          (some? size)
          (assoc :data-size                (keyword/to-dom-value size))
          (some? stretch-orientation)
          (assoc :data-stretch-orientation (keyword/to-dom-value stretch-orientation))
          (some? vertical-align)
          (assoc :data-vertical-align      (keyword/to-dom-value vertical-align))
          (some? wrap-items?)
          (assoc :data-wrap-items          (boolean              wrap-items?))))

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
  ;   :horizontal-border (keyword)(opt)
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)
  ;   :vertical-border (keyword)(opt)
  ;   :width (keyword, px or string)(opt)}
  ;
  ; @return
  ;  {:class (string or vector)
  ;   :data-color (string)
  ;   :data-font-size (string)
  ;   :data-font-weight (string)
  ;   :data-height (string)
  ;   :data-horizontal-border (string)
  ;   :data-variant (string)
  ;   :style (map)
  ;    {:height (string)
  ;     :width (string)}
  ;   :data-vertical-border (string)
  ;   :data-width (string)}
  [_ {:keys [class background-color border-color color font-size font-weight height
             horizontal-border style variant vertical-border width]}]
  (cond-> (param {})
          (some? background-color)
          (assoc :data-background-color (keyword/to-dom-value background-color))
          (some? border-color)
          (assoc :data-border-color     (keyword/to-dom-value border-color))
          :use-class
          (assoc :class                 (css/join-class "x-element" class))
          (some? color)
          (assoc :data-color            (keyword/to-dom-value color))
          (some? font-size)
          (assoc :data-font-size        (keyword/to-dom-value font-size))
          (some? font-weight)
          (assoc :data-font-weight      (keyword/to-dom-value font-weight))
          (some? style)
          (assoc :style                 (param style))
          (integer? height)
          (assoc-in [:style :height]    (css/px height))
          (string? height)
          (assoc-in [:style :height]    (param height))
          (keyword? height)
          (assoc :data-height           (keyword/to-dom-value height))
          (integer? width)
          (assoc-in [:style :width]     (css/px width))
          (string? width)
          (assoc-in [:style :width]     (param width))
          (keyword? width)
          (assoc :data-width            (keyword/to-dom-value width))
          (some? variant)
          (assoc :data-variant          (keyword/to-dom-value variant))
          (some? horizontal-border)
          (assoc :data-horizontal-border (keyword/to-dom-value horizontal-border))
          (some? vertical-border)
          (assoc :data-vertical-border   (keyword/to-dom-value vertical-border))))

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
  (cond-> (param {})
          (some? alt)            (assoc :alt alt)
          (some? src)            (assoc :src src)
          (boolean disabled?)    (assoc :data-disabled    true)
          (boolean highlighted?) (assoc :data-highlighted true)
          (boolean selectable?)  (assoc :data-selectable  true)))

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
;  [:x.app-elements/set-element-prop! :my-element :my-prop "My value"]
(a/reg-event-db :x.app-elements/set-element-prop! set-element-prop!)

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
;  [:x.app-elements/set-element-subprop! :my-element [:my-prop :my-subprop] "My value"]
(a/reg-event-db :x.app-elements/set-element-subprop! set-element-subprop!)

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
;  [:x.app-elements/remove-element-prop! :my-element :my-prop]
(a/reg-event-db :x.app-elements/remove-element-prop! remove-element-prop!)

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
;  [:x.app-elements/remove-element-subprop! :my-element [:my-prop :my-subprop]]
(a/reg-event-db :x.app-elements/remove-element-subprop! remove-element-subprop!)
