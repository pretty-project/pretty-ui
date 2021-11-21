
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v0.8.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.views
    (:require [mid-fruits.candy            :refer [param return]]
              [mid-fruits.map              :as map]
              [plugins.value-editor.engine :as engine]
              [x.app-components.api        :as components]
              [x.app-core.api              :as a :refer [r]]
              [x.app-elements.api          :as elements]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name {:edit-original? true}
;  A {:value-path ...} tulajdonságként átadott Re-Frame adatbázis útvonalon tárolt
;  érték szerkesztése közben, az aktuális érték az útvonalra íródik.
;
; @name {:on-save ...}
;  A mentés gomb megnyomásakor megtörténő esemény, amely használatával
;  egyedi mentési eljárás is megvalósítható.



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (a/dispatch [:value-editor/load! ...])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props->field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-path (item-path vector)}
  ;
  ; @param (map)
  ;  {:auto-focus? (boolean)
  ;   :value-path (item-path vector)}
  [editor-id {:keys [edit-path] :as editor-props}]
  (merge (map/inherit editor-props [:label :validator])
         {:auto-focus? true
          :value-path  edit-path}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @return (map)
  ;  {:edit-path (item-path vector)
  ;   :primary-button-label (metamorphic-content)(opt)
  ;   :required? (boolean)
  ;   :value-path (item-path vector)}
  [editor-id {:keys [edit-original? value-path] :as editor-props}]
  (merge {:primary-button-label :save!
          :required?  true
          :edit-path  (engine/default-value-path editor-id)
          :value-path (engine/default-value-path editor-id)}
         (if edit-original? {:edit-path value-path})
         (param editor-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- disable-save-button?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [field-value (r elements/get-input-value db :value-editor/editor-field)
        validator   (r engine/get-editor-prop   db editor-id :validator)]
       (boolean (or (and (some? validator)
                         (not ((:f validator) field-value)))
                    (and (r engine/get-editor-prop db editor-id :required?)
                         (r elements/field-empty?  db :value-editor/editor-field))))))

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:disable-save-button? (boolean)}
  [db [_ editor-id]]
  (merge (r engine/get-editor-props db editor-id)
         {:disable-save-button? (r disable-save-button? db editor-id)}))

(a/reg-sub ::get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r engine/get-editor-props db editor-id))

(a/reg-sub ::get-body-props get-body-props)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:disable-save-button? (boolean)
  ;   :primary-button-label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [header-id {:keys [disable-save-button? primary-button-label] :as x}]
  [elements/button ::save-button
                   {:disabled? disable-save-button?
                    :keypress  {:key-code 13 :required? true}
                    :on-click  [:value-editor/save-value! header-id]
                    :label     primary-button-label
                    :preset    :close-button}])

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [header-id _]
  [elements/button ::cancel-button
                   {:color    :default
                    :keypress {:key-code 27 :required? true}
                    :label    :cancel!
                    :preset   :close-button
                    :on-click [:x.app-tools.editor/cancel-editing! header-id]}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [header-id header-props]
  [elements/polarity ::header
                     {:start-content [cancel-button header-id header-props]
                      :end-content   [save-button   header-id header-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------
(defn- editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  ;  {:helper (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [body-id {:keys [helper]}]
  (if (some? helper)
      [:<> [elements/separator {:horizontal :horizontal :size :l}]
           [elements/text      {:content helper}]]))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [body-id body-props]
  (let [field-props (editor-props->field-props body-id body-props)]
       [:<> [elements/separator  {:horizontal :horizontal :size :l}]
            [elements/text-field :value-editor/editor-field field-props]
            [editor-helper       body-id body-props]
            [elements/separator  {:horizontal :horizontal :size :l}]]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :value-editor/load!
  ; @param (keyword)(opt) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:value-path [...]}
  ;   :helper (metamorphic-content)(opt)
  ;   :initial-value (string)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Default: false
  ;   :on-save (metamorphic-event)(opt)
  ;   :primary-button-label (metamorphic-content)(opt)
  ;    Default: :save!
  ;   :required? (boolean)(opt)
  ;    Default: true
  ;   :validator (map)(opt)(constant)
  ;    {:f (function)
  ;     :invalid-message (metamorphic-content)}
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [:value-editor/load! {...}]
  ;
  ; @usage
  ;  [:value-editor/load! :my-editor {...}]
  (fn [_ event-vector]
    (let [editor-id    (a/event-vector->second-id   event-vector)
          editor-props (a/event-vector->first-props event-vector)
          editor-props (a/prot editor-id editor-props editor-props-prototype)]
         {:dispatch-n [[:value-editor/initialize! editor-id editor-props]
                       [:value-editor/render!     editor-id editor-props]]})))

(a/reg-event-fx :value-editor/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  (fn [_ [_ editor-id _]]
      [:x.app-ui/add-popup! editor-id
                            {:content    #'body
                             :layout     :boxed
                             :label-bar  {:content    #'header
                                          :subscriber [::get-header-props editor-id]}
                             :subscriber [::get-body-props editor-id]}]))
