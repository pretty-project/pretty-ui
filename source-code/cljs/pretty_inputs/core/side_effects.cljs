
(ns pretty-inputs.core.side-effects
    (:require [pretty-elements.core.side-effects]
              [fruits.mixed.api       :as mixed]
              [fruits.vector.api      :as vector]
              [pretty-forms.api       :as pretty-forms]
              [pretty-inputs.core.env :as core.env]
              [time.api               :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.core.side-effects/*)
(def focus-input!            pretty-elements.core.side-effects/focus-element!)
(def blur-input!             pretty-elements.core.side-effects/blur-element!)
(def update-all-input-state! pretty-elements.core.side-effects/update-all-element-state!)
(def update-input-state!     pretty-elements.core.side-effects/update-element-state!)
(def clear-input-state!      pretty-elements.core.side-effects/clear-element-state!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-input-as-focused!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (keyword) input-props
  [input-id _]
  (update-all-input-state! dissoc :focused?)
  (update-input-state! input-id assoc :focused? true))

(defn unmark-input-as-focused!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (keyword) input-props
  [input-id _]
  (update-input-state! input-id dissoc :focused?))

(defn mark-input-as-changed!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (keyword) input-props
  [input-id _]
  (update-input-state! input-id assoc :changed? true))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-input-internal-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; @param (*) value
  [input-id _ value]
  (update-input-state! input-id assoc :internal-value value))

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

(defn clear-input-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id input-props]
  (set-input-internal-value! input-id input-props nil)
  (set-input-external-value! input-id input-props nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-enter-f] :as input-props}]
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-enter-f (on-enter-f input-displayed-value))))

(defn ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [emptiable? on-escape-f] :as input-props}]
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if emptiable?  (clear-input-value! input-id input-props))
       (if on-escape-f (on-escape-f input-displayed-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-input!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-empty-f] :as input-props}]
  (if-not (core.env/input-empty? input-id input-props)
          (clear-input-value!    input-id input-props)
          (if on-empty-f (on-empty-f nil))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-did-mount
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [autofocus? on-mount-f] :as input-props}]
  ; The autofocus has to be delayed; otherwise, if the input is a field the caret might shown up at the beginning
  ; of the content (instead of at its end).
  (letfn [(f0 [] (focus-input! input-id))]
         (if autofocus? (time/set-timeout! f0 50)))
  (let [provided-get-value-f #(core.env/get-input-displayed-value input-id input-props)]
       (pretty-forms/reg-form-input! input-id input-props provided-get-value-f)
       (init-input-internal-value!   input-id input-props)
       (use-input-initial-value!     input-id input-props))
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-mount-f (on-mount-f input-displayed-value))))

(defn input-will-unmount
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [autoclear? on-unmount-f] :as input-props}]
  (pretty-forms/dereg-form-input! input-id)
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-unmount-f (on-unmount-f input-displayed-value)))
  (if autoclear? (clear-input-value! input-id input-props)))

(defn input-focused
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-focus-f] :as input-props}]
  (mark-input-as-focused! input-id input-props)
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-focus-f (on-focus-f input-displayed-value))))

(defn input-left
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  [input-id {:keys [on-blur-f] :as input-props}]
  (unmark-input-as-focused! input-id input-props)
  (pretty-forms/input-left  input-id input-props)
  (let [input-displayed-value (core.env/get-input-displayed-value input-id input-props)]
       (if on-blur-f (on-blur-f input-displayed-value))))

(defn input-value-changed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ; @param (*) value
  [input-id {:keys [on-changed-f] :as input-props} value]
  (mark-input-as-changed!     input-id input-props)
  (pretty-forms/input-changed input-id input-props)
  (set-input-internal-value!  input-id input-props value)
  (set-input-external-value!  input-id input-props value)
  (if on-changed-f (on-changed-f value)))

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
           (input-value-changed input-id input-props input-updated-value)
           (if on-unselected-f (on-unselected-f input-updated-value)))
      (let [input-updated-value (option-value-f option)]
           (input-value-changed input-id input-props input-updated-value)
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
           (input-value-changed input-id input-props input-updated-value)
           (if on-unselected-f (on-unselected-f input-updated-value)))
      (let [option-value          (option-value-f option)
            input-displayed-value (core.env/get-input-displayed-value input-id input-props)
            input-updated-value   (-> input-displayed-value mixed/to-vector (vector/conj-item option-value))]
           (input-value-changed input-id input-props input-updated-value)
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

(defn render-input-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id input-props]
  (mark-input-as-focused! input-id input-props)
  (update-all-input-state! dissoc :popup-rendered?)
  (update-input-state! input-id assoc :popup-rendered? true))

(defn update-input-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id input-props]
  (render-input-popup! input-id input-props))

(defn close-input-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id _]
  (update-input-state! input-id dissoc :popup-rendered?))
