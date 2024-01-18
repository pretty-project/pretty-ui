
(ns pretty-inputs.input.subs
    (:require [re-frame.api :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-stored-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @usage
  ; (r get-input-stored-value db :my-input {...})
  ;
  ; @return (*)
  [db [_ _ {:keys [value value-path]}]]
  (or value (get-in db value-path)))

(defn get-input-options
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:options (vector)(opt)
  ;  :options-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; (r get-input-options db :my-input {...})
  ;
  ; @return (vector)
  [db [_ _ {:keys [options options-path]}]]
  (or options (get-in db options-path)))

(defn input-empty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  (let [input-stored-value (r get-input-stored-value db input-id input-props)]
       (and (-> input-stored-value seqable?)
            (-> input-stored-value empty?))))

(defn input-not-empty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  (let [input-stored-value (r get-input-stored-value db input-id input-props)]
       (or (-> input-stored-value seqable? not)
           (-> input-stored-value empty?   not))))
