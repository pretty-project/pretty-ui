
; WARNING! NOT TESTED! DO NOT USE!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.digit-field.views
    (:require [css.api                         :as css]
              [dom.api                         :as dom]
              [elements.digit-field.helpers    :as digit-field.helpers]
              [elements.digit-field.prototypes :as digit-field.prototypes]
              [hiccup.api                      :as hiccup]
              [random.api                      :as random]
              [re-frame.api                    :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- digit-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input.e-digit-field--input {:type "text"
                                ; XXX#4460 (source-code/cljs/elements/button/helpers.cljs)
                                :id (hiccup/value field-id "input")
                                :on-change #(let [v (dom/event->value %)]
                                                 (r/dispatch-sync [:x.db/set-item! (:value-path field-props) (str v)]))}])

(defn- digit-field-cover
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reduce (fn [%1 %2] (conj %1 [:div.e-digit-field--cover--digit {:on-mouse-up #(dom/focus-element! (dom/get-element-by-id (hiccup/value field-id "input")))
                                                                  ; prevent selecting
                                                                  :on-mouse-down #(.preventDefault %)}
                                                                 (string.api/get-nth-character (:value field-props) %2)]))
    [:div.e-digit-field--cover {:style {:width (-> field-props digit-field.helpers/field-props->digits-width css/px)}}]
    (range 4)))

(defn- digit-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-digit-field (digit-field.helpers/field-attributes field-id field-props)
                      [digit-field-input                    field-id field-props]
                      [digit-field-cover                    field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:digit-count (integer)
  ;    Default: 4
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [digit-field {...}]
  ;
  ; @usage
  ;  [digit-field :my-digit-field {...}]
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (let [field-props (digit-field.prototypes/field-props-prototype field-props)])))
