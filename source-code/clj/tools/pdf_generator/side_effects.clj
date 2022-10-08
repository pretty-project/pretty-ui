
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.pdf-generator.side-effects
    (:require [clj-htmltopdf.core         :refer [->pdf]]
              [server-fruits.base64       :as base64]
              [tools.pdf-generator.config :as config]))



;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn generate-pdf!
  ; @param (hiccup) n
  ; @param (map)(opt) options
  ;  {:author (string)(opt)
  ;   :base-uri (string)(opt)
  ;   :css-paths (strings in vector)(opt)
  ;    [(string) css-path]
  ;   :font-paths (maps in vector)(opt)
  ;    [{:font-family (string)
  ;      :src (string)}]
  ;   :orientation (keyword)(opt)
  ;    :landscape, :portrait
  ;    Default: :portrait
  ;   :page-size (keyword)(opt)
  ;    Default: :A4
  ;   :subject (string)(opt)
  ;   :title (string)(opt)}
  ;
  ; @usage
  ;  (pdf-generator/generate-pdf! [:html ...])
  ;
  ; @usage
  ;  (pdf-generator/generate-pdf! [:html ...] {...})
  ;
  ; @usage
  ;  (pdf-generator/generate-pdf! [:html ...] {:base-uri   "http://localhost:3000/"
  ;                                            :css-paths  ["public/css/my-style.css"]
  ;                                            :font-paths [{:font-family "Montserrat"
  ;                                                          :src "public/fonts/Montserrat/.../Montserrat-Regular.ttf"}]})
  ;
  ; @return (?)
  ([n]
   (generate-pdf! n {}))

  ([n {:keys [author base-uri css-paths font-paths orientation page-size subject title]}]
   (let [config {; DEBUG
                 ;:logging? true
                 ;:debug    {:display-html?    true
                 ;           :display-options? true}
                 :styles {:fonts       font-paths
                          :styles      css-paths}
                 :doc    {:author      author
                          :subject     subject
                          :title       title}
                 :page   {:margin      "0in"
                          :size        (or page-size   :A4)
                          :orientation (or orientation :portrait)}}]
        ; https://github.com/gered/clj-htmltopdf#usage
        (->pdf n config/GENERATOR-FILEPATH config))))

(defn generate-base64-pdf!
  ; @param (hiccup) n
  ; @param (map)(opt) options
  ;  {...}
  ;
  ; @usage
  ;  (pdf-generator/generate-base64-pdf! [:html ...])
  ;
  ; @return (string)
  ([n]
   (generate-base64-pdf! n {}))

  ([n options]
   (generate-pdf! n options)
   (base64/encode config/GENERATOR-FILEPATH
                  config/BASE64-FILEPATH)))
