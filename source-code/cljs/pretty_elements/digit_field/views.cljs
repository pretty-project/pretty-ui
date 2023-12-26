
(ns pretty-elements.digit-field.views
    (:require [dom.api                                :as dom]
              [fruits.css.api                         :as css]
              [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [fruits.string.api                      :as string]
              [pretty-elements.digit-field.attributes :as digit-field.attributes]
              [pretty-elements.digit-field.prototypes :as digit-field.prototypes]
              [pretty-elements.digit-field.utils      :as digit-field.utils]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- digit-field-input
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (pretty-elements.button.attributes#4460)
  [:input {:class     :pe-digit-field--input
           :type      "text"
           :id        (hiccup/value field-id "input")
           :on-change #(let [v (dom/event->value %)]
                            (r/dispatch-sync [:set-item! (:value-path field-props) (str v)]))}])

(defn- digit-field-cover
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reduce (fn [%1 %2] (conj %1 [:div {:class :pe-digit-field--cover--digit
                                      :on-mouse-up #(dom/focus-element! (dom/get-element-by-id (hiccup/value field-id "input")))
                                      ; prevent selecting
                                      :on-mouse-down #(.preventDefault %)}
                                     (fruits.string.api/nth-character (:value field-props) %2)]))
    [:div {:class :pe-digit-field--cover :style {:width (-> field-props digit-field.utils/field-props->digits-width css/px)}}]
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

(defn element
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @description
  ; The 'digit-field' element writes its actual value into the Re-Frame state
  ; delayed after the user stopped typing or without a delay when the user
  ; leaves the field!
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
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @usage
  ; [digit-field {...}]
  ;
  ; @usage
  ; [digit-field :my-digit-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [] ; field-props (digit-field.prototypes/field-props-prototype field-props)
            [digit-field field-id field-props]))))
