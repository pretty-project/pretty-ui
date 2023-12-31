
(ns pretty-elements.input.subs
    (:require [fruits.vector.api :as vector]
              [re-frame.api      :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:projected-value (*)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (*)
  [db [_ _ {:keys [projected-value value-path] :as input-props}]]
  (let [stored-value (get-in db value-path)]
       (if (or (= stored-value nil)
               (= stored-value ""))
           (-> projected-value)
           (-> stored-value))))

(defn get-input-options
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:options (vector)(opt)
  ;  :options-path (Re-Frame path vector)(opt)}
  ;
  ; @return (vector)
  [db [_ _ {:keys [options options-path]}]]
  ; XXX#2781 (source-code/cljs/pretty_elements/input/env.cljs)
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
  ; - Integers and keywords are not seqable values.
  ; - NILs, strings, vectors, maps, lists, etc. are seqable values.
  ; - The followings are both seqable and empty values:
  ;   NIL, "", [], {}, ()
  (let [input-value (r get-input-value db input-id input-props)]
       (and (-> input-value seqable?)
            (-> input-value empty?))))

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
; [:pretty-elements.input/get-input-value :my-input {...}]
(r/reg-sub :pretty-elements.input/get-input-value get-input-value)
