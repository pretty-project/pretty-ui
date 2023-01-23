
(ns elements.input.subs
    (:require [noop.api     :refer [return]]
              [re-frame.api :as r :refer [r]]
              [vector.api   :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:default-value (*)(opt)
  ;  :value-path (vector)}
  ;
  ; @return (*)
  [db [_ _ {:keys [default-value value-path] :as input-props}]]
  (let [stored-value (get-in db value-path)]
       (if (or (= stored-value nil)
               (= stored-value ""))
           (return default-value)
           (return stored-value))))

(defn get-input-options
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:options (vector)(opt)
  ;  :options-path (vector)(opt)}
  ;
  ; @return (vector)
  [db [_ _ {:keys [options options-path]}]]
  ; XXX#2781 (source-code/cljs/elements/input/helpers.cljs)
  (or options (get-in db options-path)))

(defn validate-input-value?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [validator]}]]
  (some? validator))

(defn prevalidate-input-value?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [validator]}]]
  (:prevalidate? validator))

(defn input-empty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  ; XXX#4410
  ; - Integers and keywords aren't seqable values.
  ; - NIL, string, vector, map, list, etc. are seqable values.
  ; - The following examples are both seqable and empty values:
  ;   nil, "", [], {}, ()
  (let [input-value (r get-input-value db input-id input-props)]
       (and (seqable? input-value)
            (empty?   input-value))))

(defn input-nonempty?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  ; XXX#4410
  (let [input-value (r get-input-value db input-id input-props)]
       (or (-> input-value seqable? not)
           (-> input-value empty?   not))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @usage
; [:elements.input/get-input-value :my-input {...}]
(r/reg-sub :elements.input/get-input-value get-input-value)
