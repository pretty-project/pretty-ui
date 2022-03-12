
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.passfield-handler.events
    (:require [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/toggle-passphrase-visibility! toggle-passphrase-visibility!)
