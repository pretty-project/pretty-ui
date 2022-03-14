
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.passfield-handler.subs
    (:require [x.app-core.api                              :as a :refer [r]]
              [x.app-elements.engine.element               :as element]
              [x.app-elements.engine.field                 :as field]
              [x.app-elements.passfield-handler.prototypes :as passfield-handler.prototypes]))



;; ----------------------------------------------------------------------------
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
  (let [toggle-props {:icon (r get-visibility-toggle-icon db field-id)}]
       (passfield-handler.prototypes/visibility-toggle-props-prototype field-id toggle-props)))

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
          :type            (r get-field-type              db field-id)}))
