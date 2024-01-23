
(ns pretty-engine.input.value.env
    (:require [pretty-engine.input.state.env :as input.state.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-changed?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id _]
  (input.state.env/get-input-state input-id :changed?))

(defn input-empty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id _]
  (let [internal-value (input.state.env/get-input-state input-id :internal-value)]
       (and (seqable? internal-value)
            (empty?   internal-value))))

(defn input-not-empty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [input-id input-props]
  (-> (input-empty? input-id input-props) not))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-internal-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (*)
  [input-id _]
  (input.state.env/get-input-state input-id :internal-value))

(defn get-input-external-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (*)
  [_ {:keys [get-value-f]}]
  (if get-value-f (get-value-f)))

(defn get-input-displayed-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (*)
  [input-id {:keys [projected-value] :as input-props}]
  (if-some [input-internal-value (get-input-internal-value input-id input-props)]
           (-> input-internal-value)
           (if-not (input-changed? input-id input-props)
                   (-> projected-value))))
