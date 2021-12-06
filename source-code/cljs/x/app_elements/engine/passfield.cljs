
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.28
; Description:
; Version: v0.3.2
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.passfield
    (:require [mid-fruits.candy              :refer [param]]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-elements.engine.field   :as field]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn visibility-toggle-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ;  {:icon (keyword)
  ;    Default: :visibility
  ;   :on-click (metamorphic-event)
  ;    Default: [...]
  ;   :tooltip (keyword)
  ;    Default: :show-password!}
  [field-id toggle-props]
  (merge {:icon     :visibility
          :on-click [:elements/toggle-passphrase-visibility! field-id]
          :tooltip  :show-password!}
         (param toggle-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn passphrase-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (boolean (r element/get-element-prop db field-id :passphrase-visible?)))

(defn get-field-type
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (keyword)
  ;  :password, :text
  [db [_ field-id]]
  (if (r passphrase-visible? db field-id) :text :password))

(defn get-visibility-toggle-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (keyword)
  ;  :visibility, :visibility_off
  [db [_ field-id]]
  (if (r passphrase-visible? db field-id) :visibility_off :visibility))

(defn get-visibility-toggle-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:icon (keyword)}
  [db [_ field-id]]
  (visibility-toggle-props-prototype field-id {:icon (r get-visibility-toggle-icon db field-id)}))

(defn get-passfield-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:end-adornments (maps in vector)
  ;   :type (keyword)}
  [db [_ field-id]]
  (merge (r field/get-field-props db field-id)
         {:end-adornments [(r get-visibility-toggle-props db field-id)]
          :type           (r get-field-type db field-id)}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-passphrase-visibility!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/update-element-prop! db field-id :passphrase-visible? not))

(a/reg-event-db :elements/toggle-passphrase-visibility! toggle-passphrase-visibility!)
