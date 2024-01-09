
(ns pretty-inputs.core.side-effects
    (:require [pretty-inputs.core.state :as core.state]
              [pretty-inputs.core.env :as core.env]
              [fruits.vector.api :as vector]
              [fruits.mixed.api :as mixed]
              [pretty-forms.api :as pretty-forms]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-input-as-focused!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [input-id]
  (reset! core.state/FOCUSED-INPUT input-id))

(defn unmark-input-as-focused!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [_]
  (reset! core.state/FOCUSED-INPUT nil))

(defn mark-input-as-changed!
  ; @ignore
  ;
  ; @param (keyword) input-id
  [input-id]
  (swap! core.state/CHANGED-INPUTS assoc input-id true))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-input-internal-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; @param (*) value
  [input-id input-props value]
  (swap! core.state/INPUT-INTERNAL-VALUES assoc input-id value))

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
  (if-let [input-external-value (core.env/get-input-external-value input-id input-props)]
          (set-input-internal-value! input-id input-props input-external-value)))

(defn use-input-initial-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [initial-value] :as input-props}]
  (when (-> (core.env/get-input-external-value input-id input-props) nil?)
        (-> (set-input-internal-value!         input-id input-props initial-value))
        (-> (set-input-external-value!         input-id input-props initial-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-did-mount
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-mounted-f] :as input-props}]
  (let [provided-get-value-f #(core.env/get-input-displayed-value input-id input-props)]
       (pretty-forms/reg-form-input! input-id input-props provided-get-value-f)
       (init-input-internal-value!   input-id input-props)
       (use-input-initial-value!     input-id input-props))
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-mounted-f (on-mounted-f input-displayed-value))))

(defn input-will-unmount
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-unmounted-f] :as input-props}]
  (pretty-forms/dereg-form-input! input-id)
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-unmounted-f (on-unmounted-f input-displayed-value))))

(defn input-focused
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-focused-f] :as input-props}]
  (mark-input-as-focused! input-id)
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-focused-f (on-focused-f input-displayed-value))))

(defn input-left
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-blurred-f] :as input-props}]
  (unmark-input-as-focused! input-id)
  (pretty-forms/input-left  input-id input-props)
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-blurred-f (on-blurred-f input-displayed-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-single-option!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  [input-id {:keys [on-selected-f on-unselected-f option-value-f] :as input-props} option]
  (if (core.env/single-option-selected? input-id input-props option)
      (let [input-updated-value nil]
           (set-input-internal-value!  input-id input-props input-updated-value)
           (set-input-external-value!  input-id input-props input-updated-value)
           (mark-input-as-changed!     input-id)
           (pretty-forms/input-changed input-id input-props)
           (if on-unselected-f (on-unselected-f input-updated-value)))
      (let [input-updated-value (option-value-f option)]
           (set-input-internal-value!  input-id input-props input-updated-value)
           (set-input-external-value!  input-id input-props input-updated-value)
           (mark-input-as-changed!     input-id)
           (pretty-forms/input-changed input-id input-props)
           (if on-selected-f (on-selected-f input-updated-value)))))

(defn toggle-multi-option!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  [input-id {:keys [on-selected-f on-unselected-f option-value-f] :as input-props} option]
  (if (core.env/multi-option-selected? input-id input-props option)
      (let [option-value          (option-value-f option)
            input-displayed-value (core.env/get-input-displayed-value input-id input-props)
            input-updated-value   (-> input-displayed-value mixed/to-vector (vector/remove-item option-value))]
           (set-input-internal-value!  input-id input-props input-updated-value)
           (set-input-external-value!  input-id input-props input-updated-value)
           (mark-input-as-changed!     input-id)
           (pretty-forms/input-changed input-id input-props)
           (if on-unselected-f (on-unselected-f input-updated-value)))
      (let [option-value          (option-value-f option)
            input-displayed-value (core.env/get-input-displayed-value input-id input-props)
            input-updated-value   (-> input-displayed-value mixed/to-vector (vector/conj-item option-value))]
           (set-input-internal-value!  input-id input-props input-updated-value)
           (set-input-external-value!  input-id input-props input-updated-value)
           (mark-input-as-changed!     input-id)
           (pretty-forms/input-changed input-id input-props)
           (if on-selected-f (on-selected-f input-updated-value)))))

(defn toggle-option!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) option
  [input-id {:keys [options] :as input-props} option]
  (if (vector/count-min? options 2)
      (toggle-multi-option!  input-id input-props option)
      (toggle-single-option! input-id input-props option)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id _]
  (mark-input-as-focused! input-id)
  (reset! core.state/RENDERED-POPUP input-id))

(defn update-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id input-props]
  (render-popup! input-id input-props))

(defn close-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [_ _]
  (reset! core.state/RENDERED-POPUP nil))
