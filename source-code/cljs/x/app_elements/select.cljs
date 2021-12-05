
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.19
; Description:
; Version: v0.8.2
; Compatibility: x4.3.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.map            :as map]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.button     :as button   :rename {view button}]
              [x.app-elements.label      :as label    :rename {view label}]
              [x.app-elements.polarity   :as polarity :rename {view polarity}]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A [select-options] komponenst megjelenítő popup UI elemet esemény-alapon is
;  lehetséges megjeleníteni, az [:elements/render-select-options! ...]
;  esemény meghívásával.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def CLOSE-POPUP-DELAY 250)

; @constant (ms)
(def AUTOCLEAR-VALUE-DELAY 650)

; @constant (ms)
(def ON-POPUP-CLOSED-DELAY 600)

; @constant (metamorphic-content)
(def DEFAULT-SELECT-BUTTON-LABEL :select!)

; @constant (keyword)
(def SELECT-BUTTON-ICON :unfold_more)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- on-select-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) options-props
  ;  {:autoclear? (boolean)(opt)
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  [select-id {:keys [autoclear? on-popup-closed on-select value-path]}]
  (let [popup-id (engine/element-id->extended-id select-id :popup)]
       {:dispatch-some  on-select
        :dispatch-later [{:ms       CLOSE-POPUP-DELAY
                          :dispatch [:ui/close-popup! popup-id]}

                         ; XXX#0134
                         (if (boolean autoclear?)
                             {:ms       AUTOCLEAR-VALUE-DELAY
                              :dispatch [:db/remove-item! value-path]})

                         (when (some? on-popup-closed)
                               {:ms       ON-POPUP-CLOSED-DELAY
                                :dispatch on-popup-closed})]}))

(defn- options-props->render-popup-label-bar?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options-props
  ;  {:options-label (metamorphic-content)(opt)
  ;
  ; @return (boolean)
  [{:keys [options-label]}]
  (some? options-label))

(defn- options-props->render-popup-cancel-bar?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options-props
  ;  {:user-cancel? (boolean)(opt)
  ;
  ; @return (boolean)
  [{:keys [user-cancel?]}]
  (boolean user-cancel?))

(defn- view-props->select-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:get-label-f (function)
  ;   :select-button-label (metamorphic-content)(opt)
  ;   :value (*)}
  ;
  ; @return (metamorphic-content)
  [{:keys [get-label-f select-button-label] :as view-props}]
  (if-let [selected-option (get view-props :value)]
          (get-label-f selected-option)
          (or select-button-label DEFAULT-SELECT-BUTTON-LABEL)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A select-button elem {:on-click ...} eseménye kirendereli
  ; a select-options elemet tartalmazó popup UI elemet.
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:as-button? (boolean)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :value-path (item-path vector)}
  [select-id {:keys [as-button?] :as select-props}]
  (let [options-props (param select-props)]
       (merge {:get-label-f  return
               :get-value-f  return
               :options-path (engine/default-options-path select-id)
               :value-path   (engine/default-value-path   select-id)}
              ; A button elemnél is alkalmazott tulajdonságok csak akkor részei a select elem
              ; tulajdonságai prototípusának, ha a select elem nem button elemként jelenik meg.
              (if-not as-button? {:layout :row})
              (param select-props)
              {:on-click [:elements/render-select-options! select-id options-props]})))

(defn- options-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) options-props
  ;  {:on-select (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :on-select (metamorphic-event)
  ;   :options-id (keyword)
  ;   :options-path (item-path vector)
  ;   :value-path (item-path vector)}
  [select-id options-props]
  (let [on-select (on-select-events select-id options-props)]
       (merge {:get-label-f  return
               :get-value-f  return
               :options-path (engine/default-options-path select-id)
               :value-path   (engine/default-value-path   select-id)}
              (param options-props)
              {:on-select  on-select
               :options-id (engine/element-id->extended-id select-id :options)})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;  select-id or options-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (merge (r engine/get-element-view-props    db element-id)
         (r engine/get-selectable-view-props db element-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Select options components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-label-bar
  ; @param (keyword) popup-id
  ; @param (map) content-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (component)
  [_ content-props]
  [polarity {:middle-content [label {:content (:label content-props)}]}])

(defn- popup-cancel-bar
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [polarity {:start-content [button {:preset   :cancel-button
                                     :on-click [:ui/close-popup! popup-id]}]}])

(defn- select-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:get-label-f (function)
  ;   :options-id (keyword)}
  ; @param (*) option
  ;
  ; @return (hiccup)
  [popup-id {:keys [get-label-f options-id] :as view-props} option]
  (let [option-label (get-label-f option)]
       [:button.x-select--option
         (engine/selectable-option-attributes options-id view-props option)
         [components/content {:content option-label}]]))

(defn- ab7081
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:options (maps in vector)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [options] :as view-props}]
  (reduce #(vector/conj-item %1 [select-option popup-id view-props %2])
           [:div.x-select--options]
           (param options)))

(defn- select-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) options-props
  ;  {:options-id (keyword)}
  ;
  ; @return (component)
  [popup-id {:keys [options-id] :as options-props}]
  [engine/stated-element options-id
    {:component     #'ab7081
     :element-props options-props
     :subscriber  [::get-view-props options-id]}])



;; -- Select button components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:i.x-select--button-icon SELECT-BUTTON-ICON])

(defn- select-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ view-props]
  (let [button-label (view-props->select-button-label view-props)]
       [:div.x-select--button-label [components/content {:content button-label}]]))

(defn- select-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [select-id view-props]
  [:button.x-select--button-body
    (engine/clickable-body-attributes select-id view-props)
    [select-button-label select-id view-props]
    [select-button-icon  select-id view-props]])

(defn- select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [select-id view-props]
  [:div.x-select--button
    [select-button-body    select-id view-props]
    [engine/element-helper select-id view-props]])

(defn- select-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  (if (some? label)
      [:div.x-select--label [components/content {:content label}]
                            (if (boolean required?)
                                [:span.x-input--label-asterisk "*"])]))

(defn- select-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [select-id view-props]
  [:div.x-select
    (engine/element-attributes select-id view-props)
    [select-label  select-id view-props]
    [select-button select-id view-props]])

(defn- select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;  {}
  ;
  ; @return (hiccup)
  [select-id {:keys [as-button?] :as view-props}]
  (if (boolean as-button?)
      (let [button-props (engine/apply-preset button/BUTTON-PROPS-PRESETS view-props)
            button-props (a/prot button-props button/button-props-prototype)]
           [button/button select-id button-props])
      [select-layout select-id view-props]))

(defn view
  ; A select elem gombja helyett lehetséges button elemet megjeleníteni az {:as-button? true}
  ; tulajdonság használatával.
  ;
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;  {:as-button? (boolean)(opt)
  ;    Default: false
  ;   :autoclear? (boolean)(opt)
  ;    Default: false
  ;   :default-value (*)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :options-label (metamorphic-content)(constant)(opt)
  ;   :options-path (item-path vector)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :user-cancel? (boolean)(constant)(opt)
  ;    Default: false
  ;    Only w/o {:options-label ...}
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/select {...}]
  ;
  ; @usage
  ;  [elements/select :my-select {...}]
  ;
  ; @usage
  ;  [elements/select {:as-button? true
  ;                    :icon       :sort
  ;                    :label      :order-by
  ;                    :layout     :icon-button
  ;                    :options-path [:my :options]
  ;                    :value-path   [:my :selected :option]}]
  ;
  ; @return (hiccup)
  ([select-props]
   [view (a/id) select-props])

  ([select-id select-props]
   (let [select-props (a/prot select-id select-props select-props-prototype)]
        [engine/stated-element select-id
                               {:component     #'select
                                :element-props select-props
                                :initializer   [:elements/init-selectable! select-id]
                                :subscriber    [::get-view-props           select-id]}])))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/render-select-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Az [:elements/render-select-options!] esemény használatával nem lehetséges
  ; megfelően inicializálni a select elemet, mert a select-options elem nem használhatja
  ; az [:elements/init-selectable!] esemény initializer-ként, ugyanis az opciók minden
  ; kirenderelésekor újrainicializálná az elemet.
  ;
  ; WARNING#0134
  ;  A [select-options] elem az opció kiválasztása után lecsatolódik a React-fából,
  ;  ezért a tulajdonságai sem maradnak elérhetők a Re-Frame adatbázisban!
  ;  A lecsatolódás után az (r elements/get-input-value db :my-select) függvény
  ;  visszatérési értéke nil!
  ;
  ; @param (keyword)(opt) select-id
  ; @param (map) options-props
  (fn [_ event-vector]
      (let [select-id     (a/event-vector->second-id   event-vector)
            options-props (a/event-vector->first-props event-vector)
            options-id    (engine/element-id->extended-id select-id :popup)
            options-props (a/prot select-id options-props options-props-prototype)]
           [:ui/add-popup! options-id
                           {:content       #'select-options
                            :content-props options-props
                            :layout        :boxed
                            :min-width     :xs

                            ; Select options popup's label-bar
                            :label-bar (cond (options-props->render-popup-label-bar?  options-props)
                                             {:content       #'popup-label-bar
                                              :content-props {:label (:options-label options-props)}}
                                             (options-props->render-popup-cancel-bar? options-props)
                                             {:content #'popup-cancel-bar})}])))
