
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.pdf-generator.side-effects
    (:require [clojure.data.codec.base64  :as b64]
              [server-fruits.io           :as io]
              [tools.pdf-generator.config :as config]

              ; TEMP
              ; Ismeretlen hiba miatt hosztolva!
              ; @Peti
              [clj-htmltopdf.core :refer [->pdf]]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn generate-pdf!
  ; @param (hiccup) n
  ; @param (map)(opt) options
  ;  {}
  ;
  ; @usage
  ;  (pdf-generator/generate-pdf! [:html [:head ...] [:body ...]])
  ;
  ; @return (?)
  ([n]
   (generate-pdf! n {}))

  ([n {}]
   (let [
         config {;:logging? true
                 ;:debug {:display-html? true
                 ;        :display-options? true
                 :base-uri "http://localhost:3000"

                 :styles {:fonts  [;{:font-family "Montserrat" :src "file:/pdf/fonts/Montserrat/fonts/ttf/Montserrat-Regular.ttf"}
                                   ;{:font-family "Montserrat" :src "http://localhost:3000/pdf/fonts/Montserrat/fonts/ttf/Montserrat-Regular.ttf"}]
                                   {:font-family "Montserrat" :src "file:///Users/bithandshake/Montserrat-Regular.ttf"}]
                                   ;{:font-family "Montserrat" :src "/Montserrat-Regular.ttf"}]
                                   ;{:font-family "Montserrat" :src "file:///pdf/fonts/Montserrat/fonts/ttf/Montserrat-Regular.ttf"}]
                                   ;{:font-family "Montserrat" :src "http://localhost:3000/pdf/fonts/Montserrat/fonts/ttf/Montserrat-Regular.ttf"}]

                          :styles [
                                   ;"http://localhost:3000/pdf/css/pdf-fonts.css"
                                   "http://localhost:3000/css/templates/pdf/blank.css"]}
                 :doc {:title   "Árajánlat"
                       :author  "Woermann - Karaváncentrum"
                       :subject "Paul Cristian"}
                 :page {:margin "0in"
                        :size   "A4"
                        :orientation :portrait}}]
        ; https://github.com/gered/clj-htmltopdf#usage
        (->pdf n config/GENERATOR-FILEPATH config))))

(defn generate-base64-pdf!
  ; @param (hiccup) n
  ; @param (map)(opt) options
  ;  {}
  ;
  ; @usage
  ;  (pdf-generator/generate-base64-pdf! [:html [:head ...] [:body ...]])
  ;
  ; @return (string)
  ([n]
   (generate-base64-pdf! n {}))

  ([n options]
   (generate-pdf! n options)
   (with-open [i (io/input-stream  config/GENERATOR-FILEPATH)
               o (io/output-stream config/BASE64-FILEPATH)]
              (b64/encoding-transfer i o))
   (slurp config/BASE64-FILEPATH)))
