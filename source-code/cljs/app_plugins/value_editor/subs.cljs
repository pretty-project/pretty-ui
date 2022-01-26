
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.9.6
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.subs
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id editor-id item-key]]
  (get-in db [extension-id :value-editor/meta-items editor-id item-key]))

(defn edit-original?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ extension-id editor-id]]
  ; WARNING!
  ; Az {:value-path nil} beállítással indított szerkesztő {:edit-path [...]} és {:value-path [...]}
  ; tulajdonságai megegyeznek, ami megfelel az {:edit-original? true} beállítás használatának,
  ; és függetlenül az {:edit-original? ...} beállítás értékétől!
  ; Ezért az edit-original? függvény nem az {:edit-original? ...} beállítás értékét vizsgálja!
  (= (r get-meta-item db extension-id editor-id :edit-path)
     (r get-meta-item db extension-id editor-id :value-path)))

(defn get-original-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [db [_ extension-id editor-id]]
  (let [value-path (r get-meta-item db extension-id editor-id :value-path)]
       (get-in db value-path)))

(defn get-editor-value
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  (r value-editor/get-editor-value db :my-extension :my-editor)
  ;
  ; @return (string)
  [db [_ extension-id editor-id]]
  (let [edit-path (r get-meta-item db extension-id editor-id :edit-path)]
       (get-in db edit-path)))

; @usage
;  [:value-editor/get-editor-value :my-extension :my-editor]
(a/reg-sub :value-editor/get-editor-value get-editor-value)

(defn get-on-save-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (metamorphic-event)
  [db [_ extension-id editor-id]]
  (if-let [on-save-event (r get-meta-item db extension-id editor-id :on-save)]
          (let [editor-value (r get-editor-value db extension-id editor-id)]
               (a/metamorphic-event<-params on-save-event editor-value))))

(defn disable-save-button?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  (let [field-value (r elements/get-input-value db :value-editor/editor-field)
        validator   (r get-meta-item            db extension-id editor-id :validator)]
       (boolean (or ; If validator is in use & field-value is NOT valid ...
                    (and validator (not ((:f validator) field-value)))
                    ; If field is required & field is empty ...
                    (and (r get-meta-item         db extension-id editor-id :required?)
                         (r elements/field-empty? db :value-editor/editor-field))))))

(defn get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:disable-save-button? (boolean)
  ;   :save-button-label (metamorphic-content)}
  [db [_ extension-id editor-id]]
  {:disable-save-button? (r disable-save-button? db extension-id editor-id)
   :save-button-label    (r get-meta-item        db extension-id editor-id :save-button-label)})

(a/reg-sub :value-editor/get-header-props get-header-props)

(defn get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:edit-path (item-path vector)
  ;   :helper (metamorphic-content)
  ;   :label (metamorphic-content)
  ;   :modifier (function)
  ;   :validator (map)}
  [db [_ extension-id editor-id]]
  {:edit-path (r get-meta-item db extension-id editor-id :edit-path)
   :helper    (r get-meta-item db extension-id editor-id :helper)
   :label     (r get-meta-item db extension-id editor-id :label)
   :modifier  (r get-meta-item db extension-id editor-id :modifier)
   :validator (r get-meta-item db extension-id editor-id :validator)})

(a/reg-sub :value-editor/get-body-props get-body-props)
