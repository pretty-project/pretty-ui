
(ns pretty-engine.input.value.side-effects
    (:require [pretty-engine.input.state.side-effects :as input.state.side-effects]
              [pretty-engine.input.value.env          :as input.value.env]
              [pretty-forms.api                       :as pretty-forms]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-input-as-changed!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (keyword) input-props
  [input-id _]
  (input.state.side-effects/update-input-state! input-id assoc :changed? true))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-input-internal-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; @param (*) value
  [input-id _ value]
  (input.state.side-effects/update-input-state! input-id assoc :internal-value value))

(defn set-input-external-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) value
  [_ {:keys [set-value-f]} value]
  (if set-value-f (set-value-f value)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-input-internal-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id input-props]
  (if-let [input-external-value (input.value.env/get-input-external-value input-id input-props)]
          (set-input-internal-value! input-id input-props input-external-value)))

(defn use-input-initial-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [initial-value] :as input-props}]
  (when (-> (input.value.env/get-input-external-value input-id input-props) nil?)
        (-> (set-input-internal-value!                input-id input-props initial-value))
        (-> (set-input-external-value!                input-id input-props initial-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-value-changed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) value
  [input-id {:keys [on-changed-f on-empty-f] :as input-props} value]
  (set-input-internal-value!  input-id input-props value)
  (set-input-external-value!  input-id input-props value)
  (mark-input-as-changed!     input-id input-props)
  (pretty-forms/input-changed input-id input-props)
  (if on-changed-f (on-changed-f value))
  (if on-empty-f   (if (input.value.env/input-empty? input-id input-props)
                       (on-empty-f nil))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-input!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-empty-f] :as input-props}]
  (when-not (input.value.env/input-empty? input-id input-props)
            (input-value-changed          input-id input-props nil)))
