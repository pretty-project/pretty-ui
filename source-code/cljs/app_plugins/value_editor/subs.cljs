
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.subs
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-editor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  (get-in db [extension-id :value-editor/meta-items editor-id]))

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
  ; @return (boolean)
  [db [_ extension-id editor-id]]
  (let [field-value (r elements/get-input-value db :value-editor/editor-field)
        validator   (r get-meta-item            db extension-id editor-id :validator)]
       (boolean (or ; If validator is in use & field-value is NOT valid ...
                    (and validator (not ((:f validator) field-value)))
                    ; If field is required & field is empty ...
                    (and (r get-meta-item         db extension-id editor-id :required?)
                         (r elements/field-empty? db :value-editor/editor-field))))))

 

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :value-editor/get-editor-props get-editor-props)

; @usage
;  [:value-editor/get-editor-value :my-extension :my-editor]
(a/reg-sub :value-editor/get-editor-value get-editor-value)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :value-editor/get-meta-item get-meta-item)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :value-editor/disable-save-button? disable-save-button?)
