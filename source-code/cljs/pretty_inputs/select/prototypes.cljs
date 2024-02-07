
(ns pretty-inputs.select.prototypes
    (:require [fruits.noop.api                   :refer [none return]]
              [fruits.vector.api                 :as vector]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-inputs.select.env          :as select.env]
              [pretty-inputs.select.side-effects :as select.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [button] :as select-props}]
  (let [on-click-f (fn [_] (pretty-inputs.engine/show-input-popup! select-id select-props))
        label      (select.env/select-button-label select-id select-props)]
       (merge button {:gap           :auto
                      :icon          :unfold_more
                      :icon-position :right
                      :label         label
                      :on-click-f    on-click-f})))

(defn icon-button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [button] :as select-props}]
  (let [on-click-f (fn [_] (pretty-inputs.engine/show-input-popup! select-id select-props))]
       (merge button {:on-click-f on-click-f})))

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [button] :as select-props}]
  (let [on-click-f (fn [_] (pretty-inputs.engine/show-input-popup! select-id select-props))]
       (merge button {:on-click-f on-click-f})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [option-field] {:keys [end-adornments]} :option-field :as select-props}]
  (let [add-option-f         (fn [%] (select.side-effects/add-option! select-id select-props %))
        add-option-adornment {:icon :add :on-click-f add-option-f}
        end-adornments       (vector/conj-item end-adornments add-option-adornment)]
       (merge {:autofocus?      true
               :border-color    :highlight
               :border-position :bottom
               :border-width    :xxs
               :outdent         {:bottom :xs :vertical :xs}
               :placeholder     "..."}
              (-> option-field)
              {:end-adornments end-adornments
               :on-enter-f     add-option-f})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [popup] :as select-props}]
  (let [on-cover-f (fn [_] (pretty-inputs.engine/hide-input-popup! select-id select-props))]
       (merge {:cover-color :black
               :fill-color  :default}
              (-> popup)
              {:on-cover-f on-cover-f})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:border-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :max-selection (integer)
  ;  :layout (keyword)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :orientation (keyword)}
  [_ {:keys [border-color] :as select-props}]
  (merge {:click-effect    :opacity
          :font-size       :s
          :hover-effect    :opacity
          :max-selection   1
          :option-helper-f none
          :option-label-f  return
          :option-value-f  return
          :layout          :select-button
          :orientation     :vertical}
         (if border-color {:border-position :all :border-width :xxs})
         (-> select-props)))
