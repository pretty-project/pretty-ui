
(ns pretty-forms.env
    (:require [pretty-forms.state :as state]
              [fruits.vector.api :as vector]
              [fruits.map.api :as map]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-form-inputs
  ; @description
  ; Returns the IDs of inputs registered with the given form ID.
  ;
  ; @param (keyword) form-id
  ;
  ; @usage
  ; (get-form-inputs :my-form)
  ; =>
  ; [:my-input :another-input]
  ;
  ; @return (keywords in vector)
  [form-id]
  (letfn [(f0 [%] (-> % :form-id (= form-id)))]
         (-> @state/FORM-INPUTS (map/filter-values f0)
                                (map/keys))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-value
  ; @description
  ; Returns the actual value of the form input registered with the given input ID.
  ;
  ; @param (keyword) input-id
  ;
  ; @return (*)
  [input-id]
  (if-let [get-input-value-f (get-in @state/FORM-INPUTS [input-id :get-input-value-f])]
          (get-input-value-f)))

(defn get-input-validators
  ; @description
  ; Returns the validators of the form input registered with the given input ID.
  ;
  ; @param (keyword) input-id
  ;
  ; @return (maps in vector)
  [input-id]
  (get-in @state/FORM-INPUTS [input-id :validators]))

(defn get-input-invalid-message
  ; @description
  ; Returns the stored invalid message (if any) of the input registered with the given input ID.
  ;
  ; @param (keyword) input-id
  ;
  ; @usage
  ; (get-input-invalid-message :my-input)
  ; =>
  ; "Please fill out this field!"
  ;
  ; @return (metamorphic-content)
  [input-id]
  (input-id @state/FORM-ERRORS))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-validation-result
  ; @description
  ; - Applies the validators of the input registered with the given input ID and returns the result of the validation.
  ; - Stops applying further validators in case of one failed, the result will contain the invalid message of the failed validator.
  ;
  ; @param (keyword) input-id
  ;
  ; @usage
  ; (get-input-validation-result :my-input)
  ; =>
  ; {:input-id :my-input :input-value "..." :input-valid? false :invalid-message "Please fill out this field!"}
  ;
  ; @usage
  ; (get-input-validation-result :my-input)
  ; =>
  ; {:input-id :my-input :input-value "..." :input-valid? true}
  ;
  ; @return (map)
  ; {:input-id (keyword)
  ;  :input-valid? (boolean)
  ;  :input-value (*)
  ;  :invalid-message (metamorphic-content)}
  [input-id]
  (let [input-value      (get-input-value      input-id)
        input-validators (get-input-validators input-id)]
       (letfn [(f0 [{:keys [f invalid-message]}]
                   (if-not (f input-value)
                           (or invalid-message :invalid-input-value)))]
              (if-let [invalid-message (vector/first-result input-validators f0)]
                      {:input-id input-id :input-value input-value :input-valid? false :invalid-message invalid-message}
                      {:input-id input-id :input-value input-value :input-valid? true}))))
