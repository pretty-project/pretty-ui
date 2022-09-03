
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.views
    (:require [mid-fruits.candy                     :refer [param return]]
              [mid-fruits.loop                      :refer [reduce-indexed]]
              [mid-fruits.vector                    :as vector]
              [reagent.api        :as reagent]
              [x.app-components.api                 :as components]
              [x.app-core.api                       :as a :refer [r]]
              [x.app-elements.combo-box.helpers :as combo-box.helpers]
              [x.app-elements.combo-box.prototypes :as combo-box.prototypes]
              [x.app-elements.engine.api            :as engine]
              [x.app-elements.input.helpers            :as input.helpers]
              [x.app-elements.text-field.prototypes :as text-field.prototypes]
              [x.app-elements.text-field.views      :as text-field.views]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name options-path
;  A combo-box elem a listában megjelenő opciókat nem paraméterként kapja,
;  ugyanis amikor változik az opciók listája, akkor a változó paraméter miatti
;  újrarenderelések hibás működéshez vezethetnek.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.text-field.views
(def text-field           text-field.views/text-field)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props->select-option-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:select-option-event (event-vector)}
  ; @param (*) option
  ;
  ; @example
  ;  (field-props->select-option-event :my-field
  ;                                    {:select-option-event [:elements/select-option!]}
  ;                                    {:label "My option"})
  ;  =>
  ;  [:elements/select-option! :my-field {:label "My option"}]
  ;
  ; @return (event-vector)
  [field-id {:keys [select-option-event]} option]
  (vector/concat-items select-option-event [field-id option]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-combo-box-surface-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @param (map)
  [db [_ field-id]]
  (merge (r engine/get-element-props   db field-id)
         (r engine/get-combo-box-props db field-id)))

(a/reg-sub :elements/get-combo-box-surface-props get-combo-box-surface-props)

(defn- get-combo-box-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @param (map)
  [db [_ field-id]]
  (merge (r engine/get-element-props   db field-id)
         (r engine/get-combo-box-props db field-id)))

(a/reg-sub :elements/get-combo-box-props get-combo-box-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- default-option-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:get-label-f (function)}
  ; @param (map) option
  [_ {:keys [get-label-f]} option]
  [:div.x-combo-box--option-label (-> option get-label-f components/content)])

(defn- combo-box-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:option-component (component)(opt)}
  ; @param (integer) option-dex
  ; @param (map) option
  [field-id {:keys [option-component] :as field-props} option-dex option]
  ; BUG#2105
  ;  A combo-box elemhez tartozó surface felületen történő on-mouse-down esemény
  ;  a mező on-blur eseményének triggerelésével jár, ami a surface felület
  ;  React-fából történő lecsatolását okozná.
  [:button.x-combo-box--option {:on-mouse-down #(do (.preventDefault %))
                                :on-mouse-up   #(do (a/dispatch [:elements.combo-box/select-option! field-id field-props]))
                               ;:data-selected ...
                                :data-highlighted (= option-dex (combo-box.helpers/get-highlighted-option-dex field-id))}
                               (if option-component [option-component         field-id field-props option]
                                                    [default-option-component field-id field-props option])])

(defn- combo-box-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (let [options (combo-box.helpers/get-rendered-options field-id field-props)]
       (letfn [(f [option-list option-dex option]
                  ;^{:key (random/generate-react-key)}
                  (conj option-list [combo-box-option field-id field-props option-dex option]))]
              [:div.x-combo-box--options {:data-hide-scrollbar true}
                                         (reduce-indexed f [:<>] options)])))

(defn- combo-box-no-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:no-options-label (metamorphic-content)}
  [field-id {:keys [no-options-label]}]
  [:div.x-combo-box--no-options-label ; BUG#2105
                                      {:on-mouse-down #(.preventDefault %)
                                       :on-mouse-up   #(a/dispatch [:elements.text-field/hide-surface! field-id])}
                                      (components/content no-options-label)])

(defn- combo-box-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-combo-box--surface [combo-box-options field-id field-props]])
                             ; Szükségtelen megjeleníteni a no-options-label feliratot.
                             ; [combo-box-no-options-label field-id field-props]

(defn- combo-box-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
 [:div (str @x.app-elements.combo-box.state/OPTION-HIGHLIGHTS)
  [text-field.views/element field-id field-props]])

(defn- combo-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reagent/lifecycles {:component-did-mount (combo-box.helpers/component-did-mount-f field-id field-props)
                       :reagent-render      (fn [_ field-props] [combo-box-structure field-id field-props])}))

(defn element
  ; XXX#0711
  ; A combo-box elem alapkomponense a text-field elem.
  ; A combo-box elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:get-label-f (function)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :initial-options (vector)(opt)
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: :no-options
  ;   :on-select (metamorphic-event)(opt)
  ;   :options (vector)(opt)
  ;   :option-component (component)(opt)
  ;    Default: x.app-elements.combo-box.views/default-option-component
  ;   :options-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/combo-box {...}]
  ;
  ; @usage
  ;  [elements/combo-box :my-combo-box {...}]
  ;
  ; @usage
  ;  [elements/combo-box {:options-path [:my-options]
  ;                       :value-path   [:my-value]}]]
  ;
  ; @usage
  ;  [elements/combo-box {:get-label-f  :name
  ;                       :options-path [:my-options]
  ;                       :value-path   [:my-value]}]]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (combo-box.prototypes/field-props-prototype field-id field-props)
         field-props (combo-box.prototypes/hack3031              field-id field-props)
         field-props (assoc field-props :surface [combo-box-surface field-id field-props])]
        [combo-box field-id field-props])))
