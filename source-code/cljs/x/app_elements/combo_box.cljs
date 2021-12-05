
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.02
; Description:
; Version: v0.8.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box
    (:require [mid-fruits.candy          :as candy :refer [param return]]
              [mid-fruits.loop           :refer [reduce-indexed]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.text-field :as text-field :refer [text-field]]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name options-path
;  A combo-box elem a listában megjelenő opciókat nem paraméterként kapja,
;  ugyanis amikor változik az opciók listája, akkor a változó paraméter miatti
;  újrarenderelések hibás működéshez vezethetnek.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (metamorphic-content)
(def DEFAULT-NO-OPTIONS-LABEL :no-options)

; @constant (event-vector)
(def DEFAULT-SELECT-OPTION-EVENT [:elements/select-option!])



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



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- combo-box-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :no-options-label (metamorphic-content)
  ;   :on-change (metamorphic-event)
  ;   :on-extend (metamorphic-event)
  ;   :select-option-event (event-vector)}
  [field-id field-props]
  (merge {:emptiable?          true
          :get-label-f         return
          :get-value-f         return
          :no-options-label    DEFAULT-NO-OPTIONS-LABEL
          :select-option-event DEFAULT-SELECT-OPTION-EVENT
          :on-extend           [:elements/add-option!]
          :options-path        (engine/default-options-path field-id)
          ; A combo-box elem használatakor nem elérhető az {:on-blur ...}
          ; és {:on-focus ...} tulajdonság, mivel a combo-box elem saját
          ; használatra lefoglalja ezeket. Szükség esetén megoldható
          ; a két tulajdonság használhatóvá tétele.
          ; Az {:on-blur ...} és {:on-focus ...} tulajdonságokat
          ; a multi-combo-box elem a combo-box elem integrálásakor felülírja.
          :on-blur  [:elements/remove-combo-box-controllers! field-id]
          :on-focus [:elements/reg-combo-box-controllers!    field-id]}
         (param field-props)
         {:on-change  [:elements/->combo-box-changed         field-id]}))

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  (let [combo-box-props (combo-box-props-prototype field-id field-props)]
       (text-field/field-props-prototype field-id combo-box-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-surface-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @param (map)
  [db [_ field-id]]
  (merge (r engine/get-element-view-props   db field-id)
         (r engine/get-combo-box-view-props db field-id)))

(a/reg-sub ::get-surface-view-props get-surface-view-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @param (map)
  [db [_ field-id]]
  (merge (r engine/get-element-view-props   db field-id)
         (r engine/get-combo-box-view-props db field-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- default-option-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:get-label-f (function)}
  ; @param (map) option
  ;
  ; @return (hiccup)
  [_ {:keys [get-label-f]} option]
  (let [option-label (get-label-f option)]
       [:div.x-combo-box--option-label [components/content {:content option-label}]]))

(defn- combo-box-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:option-component (component)(opt)
  ;   :select-option-event (event-vector)}
  ; @param (map) option-data
  ;  {:option (*)
  ;   :selected? (boolean)}
  ; @param (integer) option-dex
  ;
  ; @return (hiccup)
  [field-id {:keys [option-component select-option-event] :as view-props}
            {:keys [highlighted? option selected?]} option-dex]
  (let [select-option-event (field-props->select-option-event field-id view-props option)]
       ; BUG#2105
       ;  A combo-box elemhez tartozó surface felületen történő on-mouse-down esemény
       ;  a mező on-blur eseményének triggerelésével jár, ami a surface felület
       ;  React-fából történő lecsatolását okozná.
       [:button.x-combo-box--option {:on-mouse-down #(do (.preventDefault %))
                                     :on-mouse-up   #(do (a/dispatch-n [[:elements/hide-surface! field-id]
                                                                        (param select-option-event)]))
                                     :data-selected    (param selected?)
                                     :data-highlighted (param highlighted?)}
                                    (if (some? option-component)
                                        [option-component         field-id view-props option]
                                        [default-option-component field-id view-props option])]))

(defn- combo-box-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:rendered-options (maps in vector)
  ;   [{:option (*)
  ;     :selected? (boolean)}]}
  ;
  ; @return (hiccup)
  [field-id {:keys [rendered-options] :as view-props}]
  (reduce-indexed (fn [rendered-options option-data option-dex]
                      (vector/conj-item rendered-options
                                      ;^{:key (random/generate-react-key)}
                                        [combo-box-option field-id view-props option-data option-dex]))
                  [:div.x-combo-box--options]
                  (param rendered-options)))

(defn- combo-box-no-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:no-options-label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [field-id {:keys [no-options-label] :as view-props}]
                                      ; BUG#2105
  [:div.x-combo-box--no-options-label {:on-mouse-down #(.preventDefault %)
                                       :on-mouse-up   #(a/dispatch [:elements/hide-surface! field-id])}
                                      [components/content {:content no-options-label}]])

(defn- combo-box-extender
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:on-extend (metamorphic-event)
  ;   :value (*)}
  ;
  ; @return (hiccup)
  [field-id {:keys [on-extend value]}]
  (let [on-extend (a/metamorphic-event<-params on-extend field-id value)]
                                             ; BUG#2105
       [:button.x-combo-box--extender-button {:on-mouse-down #(.preventDefault %)
                                              :on-mouse-up   #(a/dispatch on-extend)}
                                             [:i.x-combo-box--extender-icon    (param :add)]
                                             [:div.x-combo-box--extender-label (str   value)]]))

(defn- combo-box-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [field-id view-props]
  [:div.x-combo-box--surface (if (engine/field-props->render-options?  view-props)
                                 [combo-box-options           field-id view-props])
                                ; Szükségtelen megjeleníteni a no-options-label feliratot.
                                ;[combo-box-no-options-label field-id view-props]
                             (if (engine/field-props->render-extender? view-props)
                                 [combo-box-extender          field-id view-props])])

(defn field-props<-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:surface (map)
  ;   {:base-props (map)
  ;    :content (metamorphic-content)
  ;    :subscriber (subscription vector)}}
  ;
  ; @return (map)
  ;  {:surface (map)
  ;   {:base-props (map)
  ;    :content (metamorphic-content)
  ;    :subscriber (subscription vector)}}
  [field-id {:keys [surface] :as field-props}]
  (let [surface-props (merge {:base-props field-props
                              :content    #'combo-box-surface
                              :subscriber [::get-surface-view-props field-id]}
                             (param surface))]
       (assoc field-props :surface surface-props)))

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;    Default: false
  ;   :class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :default
  ;    Default: :default
  ;   :default-value (*)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :emptiable? (boolean)(opt)
  ;    Default: true
  ;   :extendable? (boolean)(opt)
  ;    Default: false
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :left
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :s
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: DEFAULT-NO-OPTIONS-LABEL
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :max-length (integer)(opt)
  ;   :on-empty (metamorphic-event)(constant)(opt)
  ;    Only w/ {:emptiable? true}
  ;   :on-extend (metamorphic-event)(constant)(opt)
  ;    Default: [:elements/add-option! field-id value]
  ;    Only w/ {:extendable? true}
  ;   :on-reset (metamorphic-event)(constant)(opt)
  ;    Only w/ {:resetable? true}
  ;   :on-select (metamorphic-event)(constant)(opt
  ;   :on-type-ended (event-vector)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :option-component (component)(opt)
  ;    Default: x.app-elements.combo-box/default-option-component
  ;   :options-path (item-path vector)(constant)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :select-option-event (event-vector)(opt)
  ;    Default: DEFAULT-SELECT-OPTION-EVENT
  ;   :style (map)(opt)
  ;   :surface (map)(opt)
  ;    {:base-props (map)(opt)
  ;      Default: field-props
  ;     :content (metamorphic-content)(opt)
  ;      Default: #'combo-box-surface
  ;     :subscriber (subscription vector)(opt)
  ;      Default: [::get-surface-view-props field-id]}
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/combo-box {...}]
  ;
  ; @usage
  ;  [elements/combo-box :my-combo-box {...}]
  ;
  ; @usage
  ;  [elements/combo-box {:extendable?  true
  ;                       :options-path [:my-options]
  ;                       :value-path   [:my-value]}]]
  ;
  ; @usage
  ;  (a/reg-event-db :my-extender (fn [db [_ field-id value]]
  ;                                   (update-in db [:my-options] vector/conj-item {:name value})))
  ;  [elements/combo-box {:extendable?  true
  ;                       :get-label-f  #(get % :name)
  ;                       :on-extend    [:my-extender]
  ;                       :options-path [:my-options]
  ;                       :value-path   [:my-value]}]]
  ;
  ; @return (component)
  ([field-props]
   [view (a/id) field-props])

  ([field-id field-props]
   (let [field-props (a/prot               field-id field-props field-props-prototype)
         field-props (field-props<-surface field-id field-props)]
        [engine/stated-element field-id
                               {:component     #'text-field/text-field
                                :element-props field-props
                                :modifier      text-field/view-props-modifier
                                :initializer   [:elements/init-selectable! field-id]
                                :subscriber    [::get-view-props           field-id]}])))
