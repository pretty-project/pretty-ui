
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.form-data
    (:require [dom.file-selector :as file-selector]
              [mid-fruits.candy  :refer [return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-to-form-data!
  ; @param (FormData object)
  ; @param (keyword or string) prop-key
  ; @param (*) prop-value
  ;
  ; @usage
  ;  (def my-form-data (js/FormData.))
  ;  (dom/append-to-form-data! my-form-data :name "John")
  ;
  ; @return (FormData object)
  [form-data prop-key prop-value]
  (let [prop-key (if (keyword? prop-key)
                     (name     prop-key)
                     (return   prop-key))]
       (.append form-data prop-key prop-value)

       ; Ez szükséges? Az .append függvény nem a form-data objektummal tér vissza?
       (return  form-data)))

(defn merge-to-form-data!
  ; @param (FormData object) form-data
  ; @param (list of maps) xyz
  ;
  ; @usage
  ;  (def my-form-data (js/FormData.))
  ;  (dom/merge-to-form-data! my-form-data {...})
  ;
  ; @usage
  ;  (def my-form-data (js/FormData.))
  ;  (dom/merge-to-form-data! my-form-data {...} {...} {...})
  ;
  ; @return (FormData object)
  [form-data & xyz]
  (doseq [n xyz]
         (doseq [[k v] n]
                (append-to-form-data! form-data k v)))

  ; Ez szükséges? Az .append függvény nem a form-data objektummal tér vissza?
  (return form-data))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-selector->form-data
  ; @param (DOM-element) file-selector
  ; @param (strings in vector)(opt) filtered-file-keys
  ;
  ; @usage
  ;  (def my-file-selector (dom/get-elementy-by-id "my-file-selector"))
  ;  (dom/file-selector->form-data my-file-selector)
  ;
  ; @usage
  ;  (def my-file-selector (dom/get-elementy-by-id "my-file-selector"))
  ;  (dom/file-selector->form-data my-file-selector ["0" "1" "4"])
  ;
  ; @return (FormData object)
  [file-selector & [filtered-file-keys]]
  (let [files     (file-selector/file-selector->files file-selector)
        form-data (js/FormData.)
        file-keys (.keys js/Object files)
        file-keys (or filtered-file-keys file-keys)]
       (doseq [file-key file-keys]
              (let [file (aget files file-key)]
                   (append-to-form-data! form-data file-key file)))

       ; Ez szükséges? Az .append függvény nem a form-data objektummal tér vissza?
       (return form-data)))
