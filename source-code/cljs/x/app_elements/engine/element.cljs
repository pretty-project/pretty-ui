
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.css     :as css]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :refer [dissoc-in]]
              [mid-fruits.vector  :as vector]
              [re-frame.api       :as r :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.element.helpers :as element.helpers]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:horizontal-align (keyword)(opt)
  ;    :center, :left, :right, :space-between
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :none, :right
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
  [_ {:keys [border-radius gap horizontal-align horizontal-position indent layout orientation position
             stretch-orientation vertical-align vertical-position]}]

  (cond-> {} horizontal-align    (assoc :data-horizontal-align    horizontal-align)
             horizontal-position (assoc :data-horizontal-position horizontal-position)

             ; TEMP
             ; TESZTELÉS ALATT:
             border-radius (assoc :data-border-radius border-radius)
             gap           (assoc :data-gap           gap)
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
  ;   :icon-position (keyword)(opt)
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)}
  ;
  ; @return
  ;  {:data-color (keyword)
  ;   :data-font-size (keyword)
  ;   :data-font-weight (keyword)
  ;   :data-hover-color (keyword)
  ;   :data-icon-family (keyword)
  ;   :data-icon-position (keyword)
  ;   :data-variant (keyword)
  ;   :style (map)
  ;    {:height (string or keyword)
  ;     :width (string or keyword)}}
  [_ {:keys [background-color border-color color font-size font-weight hover-color icon-family icon-position variant]}]
  (cond-> {} background-color (element.helpers/apply-color :background-color :data-background-color background-color)
             border-color     (element.helpers/apply-color :border-color     :data-border-color     border-color)
             color            (element.helpers/apply-color :color            :data-color            color)
             hover-color      (element.helpers/apply-color :hover-color      :data-hover-color      hover-color)
             font-size        (assoc :data-font-size     font-size)
             font-weight      (assoc :data-font-weight   font-weight)
            ;icon-family      (assoc :data-icon-family   icon-family)
             icon-family      (assoc :data-icon-position icon-position)

             variant          (assoc :data-variant       variant)))

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
  ;     :min-width (string)
  ;     :width (string)}}
  [_ {:keys [height min-width size width]}]
  (cond-> {} height     (element.helpers/apply-dimension :height    :data-height    height)
             width      (element.helpers/apply-dimension :width     :data-width     width)
             min-width  (element.helpers/apply-dimension :min-width :data-min-width min-width)
             size       (assoc                       :data-size       size)))

(defn element-generic-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:alt (string)(opt)
  ;   :selectable? (boolean)(opt)
  ;   :src (string)(opt)}
  ;
  ; @return
  ;  {:alt (string)
  ;   :data-selectable (boolean)
  ;   :src (string)}
  [_ {:keys [alt hover-color selectable? src]}]
  (cond-> {} alt (assoc :alt alt)
             src (assoc :src src)
             (some? selectable?) (assoc :data-selectable selectable?)
             (nil?  selectable?) (assoc :data-selectable false)))

(defn element-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map)(opt) element-attributes
  ;
  ; @return (map)
  [element-id element-props & [element-attributes]]
  (merge (element.helpers/element-default-attributes   element-id element-props)
         (element-layout-attributes    element-id element-props)
         (element-style-attributes     element-id element-props)
         (element-dimension-attributes element-id element-props)
         (element.helpers/element-indent-attributes    element-id element-props)
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
  (get-in db [:elements/primary :data-items element-id]))

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
  (assoc-in db [:elements/primary :data-items element-id prop-key] prop-value))


; @usage
;  [:elements/set-element-prop! :my-element :my-prop "My value"]
(r/reg-event-db :elements/set-element-prop! set-element-prop!)

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
  (let [prop-path (vector/concat-items [:elements/primary :data-items element-id] prop-path)]
       (assoc-in db prop-path prop-value)))

; @usage
;  [:elements/set-element-subprop! :my-element [:my-prop :my-subprop] "My value"]
(r/reg-event-db :elements/set-element-subprop! set-element-subprop!)

(defn remove-element-prop!
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ;
  ; @usage
  ;  (r element/remove-element-prop! db :my-element :my-prop)
  ;
  ; @return (map)
  [db [_ element-id prop-key]]
  (dissoc-in db [:elements/primary :data-items element-id prop-key]))

; @usage
;  [:elements/remove-element-prop! :my-element :my-prop]
(r/reg-event-db :elements/remove-element-prop! remove-element-prop!)

(defn remove-element-subprop!
  ; @param (keyword) element-id
  ; @param (vector) prop-path
  ;
  ; @usage
  ;  (r element/remove-element-subprop! db :my-element [:my-prop :my-subprop])
  ;
  ; @return (map)
  [db [_ element-id prop-path]]
  (let [prop-path (vector/concat-items [:elements/primary :data-items element-id] prop-path)]
       (dissoc-in db prop-path)))

; @usage
;  [:elements/remove-element-subprop! :my-element [:my-prop :my-subprop]]
(r/reg-event-db :elements/remove-element-subprop! remove-element-subprop!)
