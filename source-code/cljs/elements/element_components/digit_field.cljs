
; WARNING! NOT TESTED! DO NOT USE!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element-components.digit-field
    (:require [mid-fruits.candy                :refer [param]]
              [css.api                         :as css]
              [dom.api                         :as dom]
              [elements.engine.api             :as engine]
              [elements.input.helpers          :as input.helpers]
              ;[elements.passfield-handler.subs :as passfield-handler.subs]
              [elements.target-handler.helpers :as target-handler.helpers]
              [mid-fruits.random               :as random]
              [mid-fruits.vector               :as vector]
              [re-frame.api                    :as r :refer [r]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def DIGIT-WIDTH 36)

; @constant (px)
(def DIGIT-GAP 12)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props->digits-width
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (integer)
  [field-props]
  (+ (* DIGIT-WIDTH 4)
     (* DIGIT-GAP   3)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:digit-count (integer)
  ;   :value-path (vector)}
  [field-id field-props]
  (merge {:digit-count 4
          :value-path  (input.helpers/default-value-path field-id)}
         (param field-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-digit-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]])

(r/reg-sub :elements/get-digit-field-props get-digit-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- digit-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input.e-digit-field--input {:type "text"
                                :id (target-handler.helpers/element-id->target-id field-id)
                                :on-change #(let [v (dom/event->value %)]
                                                 (r/dispatch-sync [:db/set-item! (:value-path field-props) (str v)]))}])

(defn- digit-field-cover
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reduce (fn [%1 %2] (conj %1 [:div.e-digit-field--cover--digit {:on-mouse-up #(dom/focus-element! (dom/get-element-by-id (target-handler.helpers/element-id->target-id field-id)))
                                                                  ; prevent selecting
                                                                  :on-mouse-down #(.preventDefault %)}
                                                                 (mid-fruits.string/get-nth-character (:value field-props) %2)]))
    [:div.e-digit-field--cover {:style {:width (-> field-props field-props->digits-width css/px)}}]
    (range 4)))

(defn- digit-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.e-digit-field (engine/element-attributes field-id field-props)
                      [digit-field-input         field-id field-props]
                      [digit-field-cover         field-id field-props]])

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
   (let [field-props (field-props-prototype field-id field-props)])))
