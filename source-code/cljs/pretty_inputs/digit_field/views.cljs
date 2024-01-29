
(ns pretty-inputs.digit-field.views
    (:require [dom.api                              :as dom]
              [fruits.css.api                       :as css]
              [fruits.random.api                    :as random]
              [fruits.string.api                    :as string]
              [pretty-inputs.digit-field.attributes :as digit-field.attributes]
              [pretty-inputs.digit-field.prototypes :as digit-field.prototypes]
              [pretty-inputs.digit-field.utils      :as digit-field.utils]
              [re-frame.api                         :as r]
              [reagent.api :as reagent]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- digit-field-input
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input {:class     :pi-digit-field--input
           :type      "text"
           :id :xxx
           :on-change #(let [v (dom/event->value %)]
                            (r/dispatch-sync [:set-item! (:value-path field-props) (str v)]))}])

(defn- digit-field-cover
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reduce (fn [%1 %2] (conj %1 [:div {:class :pi-digit-field--cover--digit
                                      :on-mouse-up #(dom/focus-element! (dom/get-element-by-id "xxx"))
                                      ; prevent selecting
                                      :on-mouse-down #(.preventDefault %)}
                                     (fruits.string.api/nth-character (:value field-props) %2)]))
    [:div {:class :pi-digit-field--cover :style {:width (-> field-props digit-field.utils/field-props->digits-width css/px)}}]
    (range 4)))

(defn- digit-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div (digit-field.attributes/field-attributes field-id field-props)
        [digit-field-input                       field-id field-props]
        [digit-field-cover                       field-id field-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-lifecycles
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    field-id field-props))
                       :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount field-id field-props))
                       :reagent-render         (fn [_ field-props] [digit-field field-id field-props])}))

(defn input
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:digit-count (integer)
  ;   Default: 4
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :theme (keyword)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @usage
  ; [digit-field {...}]
  ;
  ; @usage
  ; [digit-field :my-digit-field {...}]
  ([field-props]
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (pretty-presets.engine/apply-preset           field-id field-props)
             field-props (digit-field.prototypes/field-props-prototype field-id field-props)]
            [input-lifecycles field-id field-props]))))
