
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
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.map            :as map]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.button     :refer [view] :rename {view button}]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.label      :refer [view] :rename {view label}]
              [x.app-elements.polarity   :refer [view] :rename {view polarity}]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A [select-options] komponenst megjelenítő popup UI elemet esemény-alapon is
;  lehetséges megjeleníteni, az [:x.app-elements/render-select-options! ...]
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

; @constant (keyword) Material icon class
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
                          :dispatch [:x.app-ui/close-popup! popup-id]}

                         ; XXX#0134
                         (if (boolean autoclear?)
                             {:ms       AUTOCLEAR-VALUE-DELAY
                              :dispatch [:x.app-db/remove-item! value-path]})

                         (when (some? on-popup-closed)
                               {:ms       ON-POPUP-CLOSED-DELAY
                                :dispatch on-popup-closed})]}))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-props->value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#2043
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:value-path (item-path vector)}
  ;
  ; @return (item-path vector)
  [select-id {:keys [value-path]}]
  (or (param value-path)
      (engine/default-value-path select-id)))

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
  ;  {:select-button-label (metamorphic-content)(opt)}
  ;
  ; @return (metamorphic-content)
  [{:keys [select-button-label] :as view-props}]
  (if-let [selected-option (engine/view-props->selected-option view-props)]
          (:label selected-option)
          (or select-button-label DEFAULT-SELECT-BUTTON-LABEL)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A select-button elem {:on-click ...} eseménye kirendereli
  ; a select-options elemet tartalmazó popup UI elemet.
  ;
  ; XXX#2043
  ;  Ha a select-button elem nem kap paraméterként {:value-path [...]}
  ;  tulajdonságot, akkor generál egyet magának.
  ;  Fontos, hogy a generált {:value-path [...]} tulajdonság megegyezzen
  ;  a select-button és select-options elemeknél!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:value-path (item-path vector)}
  ;
  ; @return (map)
  ;  {:layout (keyword)
  ;   :value-path (item-path vector)}
  [select-id select-props]
        ; XXX#2043
  (let [value-path    (select-props->value-path select-id select-props)
        options-props (assoc select-props :value-path value-path)]
       (merge {:layout     :row
               :value-path value-path}
              (param select-props)
              {:on-click [:x.app-elements/render-select-options! select-id options-props]})))

(defn- options-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) options-props
  ;  {:on-select (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:on-select (metamorphic-event)
  ;   :select-id (keyword)
  ;   :value-path (item-path vector)}
  [select-id options-props]
  (let [on-select (on-select-events select-id options-props)]
       (merge {:value-path (engine/default-value-path select-id)}
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
  ;
  ; @return (map)
  [db [_ element-id]]
  (merge (r engine/get-element-view-props    db element-id)
         (r engine/get-selectable-view-props db element-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
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
                                     :on-click [:x.app-ui/close-popup! popup-id]}]}])

(defn- select-option-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ; @param (map) option-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (hiccup or nil)
  [_ _ {:keys [icon]}]
  (if (some? icon)
      [:i.x-select--option-icon (keyword/to-dom-value icon)]))

(defn- select-option-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ; @param (map) option-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ _ {:keys [label]}]
  [:div.x-select--option-label [components/content {:content label}]])

(defn- select-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:select-id (keyword)
  ; @param (map) option-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [options-id] :as view-props} {:keys [label] :as option-props}]
  [:button.x-select--option
    (engine/selectable-option-attributes options-id view-props option-props)
    [select-option-icon  popup-id view-props option-props]
    [select-option-label popup-id view-props option-props]])

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
  [engine/container options-id
    {:base-props  options-props
     :component   ab7081
     :initializer [:x.app-elements/init-input! options-id]
     :subscriber  [::get-view-props            options-id]}])

(defn- select-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:i.x-select--button-icon (keyword/to-dom-value SELECT-BUTTON-ICON)])

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
    [select-button-body select-id view-props]])

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
                            (if required? "*")]))

(defn- select
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

(defn view
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :default-value (*)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :icon (keyword)(opt) Material icon class
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :options (maps in vector)(constant)
  ;    [{:icon (keyword)(opt) Material icon class
  ;      :label (metamorphic-content)
  ;      :value (*)}]
  ;   :options-label (metamorphic-content)(constant)(opt)
  ;   :request-id (keyword)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :status-animation? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:request-id ...}
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
  ; @return (hiccup)
  ([select-props]
   [view nil select-props])

  ([select-id select-props]
   (let [select-id    (a/id   select-id)
         select-props (a/prot select-id select-props select-props-prototype)]
        [engine/container select-id
          {:base-props select-props
           :component  select
           :subscriber [::get-view-props select-id]}])))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/render-select-options!
  ; WARNING#0134
  ;  A [select-options] elem az opció kiválasztása után lecsatolódik a React-fából,
  ;  ezért a tulajdonságai sem maradnak elérhetők a Re-Frame adatbázisban!
  ;  A lecsatolódás után az (r elements/get-input-value db :my-select) függvény
  ;  visszatérési értéke nil!
  ;
  ; @param (keyword)(opt) select-id
  ; @param (map) options-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :default-value (*)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a kiválasztott értéket.
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a kiválasztott értéket.
  ;   :options-label (metamorphic-content)(constant)(opt)
  ;   :options (maps in vector)(constant)
  ;    [{:icon (keyword)(opt) Material icon class
  ;      :label (metamorphic-content)
  ;      :value (*)}]
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :select-button-label (metamorphic-content)(opt)
  ;    Default: DEFAULT-SELECT-BUTTON-LABEL
  ;   :user-cancel? (boolean)(constant)(opt)
  ;    Default: false
  ;    Only w/o {:options-label ...}
  ;   :value-path (item-path vector)(constant)(opt)}
  (fn [_ event-vector]
      (let [select-id     (a/event-vector->second-id   event-vector)
            options-props (a/event-vector->first-props event-vector)
            options-props (a/prot select-id options-props options-props-prototype)]
           [:x.app-ui/add-popup!
             (engine/element-id->extended-id select-id :popup)
             {:content       #'select-options
              :content-props options-props
              :layout        :boxed
              :min-width     :xxs

              ; Select options popup's label-bar
              :label-bar (cond (options-props->render-popup-label-bar?  options-props)
                               {:content       #'popup-label-bar
                                :content-props {:label (:options-label options-props)}}
                               (options-props->render-popup-cancel-bar? options-props)
                               {:content #'popup-cancel-bar})}])))
